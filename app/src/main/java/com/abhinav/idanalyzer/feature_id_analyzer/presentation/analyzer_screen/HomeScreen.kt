package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.HomeNavigationDrawer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.TabScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.AuthScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.HomeDetails
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.AnalyzerScreenToolBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: AnalyzerViewModel = hiltViewModel(),
    navController: NavController
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
            navController.navigate(AuthScreen.Login.route)
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
                navController = navController
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
    navController: NavController
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