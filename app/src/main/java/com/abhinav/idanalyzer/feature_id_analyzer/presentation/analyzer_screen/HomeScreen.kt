package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abhinav.idanalyzer.R
import com.abhinav.idanalyzer.core.util.DataManager
import com.abhinav.idanalyzer.feature_id_analyzer.MainActivity
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.HomeNavigationDrawer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.TabScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.toInstant
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.AuthScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.HomeDetails
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.notification.NotificationHelper
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.AnalyzerScreenToolBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: AnalyzerViewModel = hiltViewModel(),
    navController: NavController,
    activity : ComponentActivity
){
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isNotificationsEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isNotificationsEnabled) {
        //TODO : Implement Notifications
    }

    HomeNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        onWhatsNewClicked = {
            navController.navigate(HomeDetails.WhatsNew.route)
            changeDrawerState(drawerState, scope)
        },
        onLogOutClicked = {
            viewModel.logOut()
            navController.navigate(AuthScreen.Login.route){
                popUpTo(AuthScreen.Login.route){
                    inclusive = true
                }
            }
            changeDrawerState(drawerState, scope)
        },
        onTurnOffNotifications = {
            isNotificationsEnabled = it
            changeDrawerState(drawerState, scope)
        },
        isNotificationsEnabled = isNotificationsEnabled,
        content = {
            SetUpScaffold(
                scrollBehaviour = scrollBehaviour,
                viewModel = viewModel,
                drawerState = drawerState,
                scope = scope,
                navController = navController,
                context = activity.applicationContext
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpScaffold(
    modifier: Modifier = Modifier,
    scrollBehaviour : TopAppBarScrollBehavior,
    viewModel: AnalyzerViewModel,
    drawerState: DrawerState,
    scope : CoroutineScope,
    navController: NavController,
    context: Context
){

    var openNavDrawer by remember{ mutableStateOf(false) }

    if(openNavDrawer){
        changeDrawerState(
            drawerState = drawerState,
            scope = scope
        )
        openNavDrawer = false
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            AnalyzerScreenToolBar(
                modifier = Modifier.fillMaxWidth(),
                onNotificationClicked = {
                    navController.navigate(HomeDetails.Notification.route)
                },
                onNavigationDrawerClicked = {
                    openNavDrawer = true
                },
                scrollBehavior = scrollBehaviour
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.surface

    ) { paddingValues ->

        val scrollState = rememberScrollState()
        val state by viewModel.state.collectAsState()
        val lastItem = state.listWithoutId.firstOrNull()

        if(lastItem != null){
            showNotification(lastItem, context = context)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = scrollState)
                .padding(paddingValues)
        ){
            TabScreen(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        }
    }
}


private fun showNotification(item: IdAnalyzer, context: Context){
    val itemInstant = item.date.toInstant()
    val currentInstant = Instant.now()
    val diff = Duration.between(currentInstant, itemInstant).seconds
    Log.e("HomeScreen" , "diff -> $diff")
    if(diff <= 5 * 1000){

        val manager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val NOTIFICATION_ID = 123

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT // Update any existing pending intent with the same request code
        )

        manager.notify( NOTIFICATION_ID ,
            NotificationCompat.Builder(context, "WeatherInformation")
                .setContentTitle("ScanSage")
                .setContentText("Suspicious activity detected")
                .setSmallIcon(R.drawable.search)
                .setLargeIcon(DataManager.getImageFromByteArray(item.imageData))
                .setContentIntent(pendingIntent)
                .build()
        )
    }
}

fun changeDrawerState(
    drawerState: DrawerState,
    scope : CoroutineScope,
) {
    scope.launch {
        if(drawerState.isOpen){
            drawerState.close()
        }else
            drawerState.open()
    }
}