package com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abhinav.idanalyzer.core.util.Constants
import com.abhinav.idanalyzer.feature_id_analyzer.MainScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.LogInScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.SignUpScreen
import io.realm.kotlin.mongodb.App

@Composable
fun RootNavigationGraph(
    activity: ComponentActivity,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = getStartDestination()
    ) {
        composable(route = Graph.HOME){
            MainScreen(activity = activity)
        }
        composable(
            route = AuthScreen.Login.route
        ){
            LogInScreen(navController = navController)
        }

        composable(
            route = AuthScreen.Register.route
        ){
            SignUpScreen(navController = navController)
        }
    }
}


sealed class AuthScreen(val route: String){
    data object Login: AuthScreen(route = "login")
    data object Register: AuthScreen(route = "register")
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val NAV_DRAWER = "nav_drawer_graph"
}

private fun getStartDestination(): String{
    val currentUser = App.create(Constants.APP_ID).currentUser
    return if(currentUser == null) AuthScreen.Login.route
    else Graph.HOME
}