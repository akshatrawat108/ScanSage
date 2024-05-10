package com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.HomeScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.findings.FindingsScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.new_features.WhatsNewScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.notification.NotificationScreen

@Composable
fun HomeNavGraph(
    activity: ComponentActivity,
    navController: NavHostController,
    innerPadding : PaddingValues
){
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(
            route = BottomBarScreen.Home.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = BottomBarScreen.Findings.route
        ){
            FindingsScreen(navController = navController)
        }
        composable(
            route = BottomBarScreen.Profile.route
        ){

        }

        homeDetailsNavGraph(navController = navController, activity = activity)
    }
}


fun NavGraphBuilder.homeDetailsNavGraph(
    navController: NavHostController,
    activity: ComponentActivity
){
    navigation(
        route = Graph.NAV_DRAWER,
        startDestination = HomeDetails.WhatsNew.route 
    ) {
        composable(
            route = HomeDetails.WhatsNew.route
        ){
            WhatsNewScreen(navController = navController)  
        }
        composable(
            route = HomeDetails.SupportUs.route
        ){
            
        }
        composable(
            route = HomeDetails.Download.route
        ){
            
        }
        composable(
            route = HomeDetails.Notification.route
        ){
            NotificationScreen(
                activity = activity,
                navController = navController
            )
        }
    }

}

sealed class HomeDetails(val route: String){
    data object SupportUs: HomeDetails(route = "support_us")
    data object Download: HomeDetails(route = "download")
    data object WhatsNew: HomeDetails(route = "whats_new")
    data object Notification : HomeDetails(route = "notification")
}