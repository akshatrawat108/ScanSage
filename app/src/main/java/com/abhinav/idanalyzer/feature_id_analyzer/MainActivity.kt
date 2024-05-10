package com.abhinav.idanalyzer.feature_id_analyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.abhinav.idanalyzer.core.theme.IdAnalyzerTheme
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.navigation.RootNavigationGraph
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdAnalyzerTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                RootNavigationGraph(
                    navController = navController,
                    activity = this@MainActivity
                )
            }

        }
    }

}
