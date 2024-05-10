package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.AnalyzerViewModel
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.WithIdScreen
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.WithoutIdScreen

@Composable
fun TabScreen(
    modifier : Modifier = Modifier,
    viewModel : AnalyzerViewModel
){
    val tabList = listOf(
        "With Id",
        "Without Id"
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
    ){
        TabRow(
            selectedTabIndex = selectedTabIndex ,
            indicator = {tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.surface
                )
            },
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            tabList.forEachIndexed {index, title->
                val fontWeight = if(index == selectedTabIndex) FontWeight.SemiBold else FontWeight.Normal
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = fontWeight,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.surface,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        when(selectedTabIndex){
            0 ->{
                Column {
                    if(state.listWithId.isEmpty()){
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }else{
                        Spacer(modifier = Modifier.height(16.dp))
                        WithIdScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel
                        )
                    }
                }
            }
            1 ->{
                Column {
                    if(state.listWithId.isEmpty()){
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }else{
                        Spacer(modifier = Modifier.height(16.dp))
                        WithoutIdScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

    }
}