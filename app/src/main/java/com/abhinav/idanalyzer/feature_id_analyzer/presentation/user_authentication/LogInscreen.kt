package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.AuthScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.Graph
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.components.AuthLogInComponent
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.viewmodel.AuthenticationEvent
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.viewmodel.AuthenticationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LogInScreen(
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavController,
) {
    val errorState by authenticationViewModel.credentialsValidateState.collectAsState()
    val state by authenticationViewModel.state.collectAsState()
    var isError by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    isError = errorState.isError
    isAuthenticated = state.isAuthenticated

    if(isAuthenticated){
        navController.navigate(Graph.HOME){
            popUpTo(Graph.AUTHENTICATION){
                inclusive = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Id analyzer",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    actionIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(24.dp))

            AuthLogInComponent(
                modifier = Modifier.fillMaxWidth(),
                onSubmit = { username, password ->
                    authenticationViewModel.onAction(
                        AuthenticationEvent.LogInWithEmail(
                            username,
                            password
                        )
                    )
                },
                errorMessage = errorState.errorMessage,
                isError = isError,
                onValueChange = {
                    isError = false
                },
                isLoading = state.isLoading,
                onBeginSignUp = {
                    navController.navigate(AuthScreen.Register.route)
                }
            )
        }

    }
}