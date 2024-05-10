package com.abhinav.idanalyzer.feature_id_analyzer

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.BottomBarScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.HomeNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    activity: ComponentActivity,
    navController: NavHostController = rememberNavController()
){

    Scaffold(
        bottomBar = {
            BottomBar(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
    ){innerPadding->
        HomeNavGraph(
            navController = navController,
            innerPadding = innerPadding,
            activity = activity
        )
    }

}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Findings,
        BottomBarScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }

    // Only Show Navigation drawer when the screen is either Home or Profile or Findings
    if(bottomBarDestination){
        NavigationBar(
            modifier = modifier.background(MaterialTheme.colorScheme.onPrimaryContainer),
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            windowInsets = NavigationBarDefaults.windowInsets
        ) {
            screens.forEachIndexed { index, bottomBarScreen ->
                AddItem(
                    screen = bottomBarScreen,
                    navController = navController,
                    isSelected =  currentDestination?.hierarchy?.any{
                        it.route == bottomBarScreen.route
                    } == true
                )
            }
        }
    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    navController: NavController,
    isSelected : Boolean
){
    NavigationBarItem(
        label = {
            val fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal

            Text(
                text = screen.title,
                fontWeight = fontWeight
            )
        },
        icon = {
               if(isSelected)
                   Icon(imageVector = screen.selectedIcon, contentDescription = screen.title)
               else
                   Icon(imageVector = screen.unselectedIcon, contentDescription = screen.title)
        },
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.surface,
            unselectedIconColor = MaterialTheme.colorScheme.surface,
            unselectedTextColor = MaterialTheme.colorScheme.surface,
            selectedTextColor = MaterialTheme.colorScheme.surface
        )
    )
}
