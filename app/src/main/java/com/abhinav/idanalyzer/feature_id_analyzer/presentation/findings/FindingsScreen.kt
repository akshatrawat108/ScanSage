package com.abhinav.idanalyzer.feature_id_analyzer.presentation.findings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.EntryListItem
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.findings.components.TimePickerDialog
import io.realm.kotlin.types.RealmInstant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FindingViewModel = hiltViewModel()
){
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    var pickedTime2 by remember {
        mutableStateOf(LocalTime.NOON)
    }

    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }
    val formattedTime2 by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime2)
        }
    }

    var entries by remember {
        mutableIntStateOf(-1)
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { 
                    Text(
                        text = "Findings",
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                actions = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Row(
                            modifier = Modifier.align(Alignment.CenterStart)
                        ){
                            Text(
                                modifier = Modifier,
                                text ="From : $formattedTime",
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier,
                                text ="To : $formattedTime2",
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start
                            )
                        }
                        if(entries != -1){
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                text ="$entries found",
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 17.sp,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->  

        var fromRealmInstance by remember {
            mutableStateOf<RealmInstant?>(null)
        }

        var endRealmInstance by remember {
            mutableStateOf<RealmInstant?>(null)
        }

        var localDate1 = LocalDate.now()
        var localDate2 = LocalDate.now()
        var localTime1 = LocalTime.NOON
        var localTime2 = LocalTime.NOON

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ){
            TimePickerDialog(
                modifier = Modifier.fillMaxWidth(),
                onDatePicked = {date->
                    localDate1 = date
                },
                onTimePicked = {time->
                    localTime1 = time
                    pickedTime = time
                },
                count = "1st"
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimePickerDialog(
                modifier = Modifier.fillMaxWidth(),
                onDatePicked = {date->
                    localDate2= date
                },
                onTimePicked = {time->
                    localTime2 = time
                    pickedTime2 = time
                },
                count = "2nd"
            )

            val localDateTime1 = localDate1.atTime(localTime1)
            val localDateTime2 = localDate2.atTime(localTime2)
            fromRealmInstance = RealmInstant.from(
                localDateTime1.toEpochSecond(ZoneOffset.UTC),
                0
            )
            endRealmInstance = RealmInstant.from(
                localDateTime2.toEpochSecond(ZoneOffset.UTC),
                0
            )

           if(fromRealmInstance == null || endRealmInstance == null){
               Box {
                   Text(
                       modifier = Modifier.align(Alignment.Center),
                       text = "Select time to get list of entries",
                       color = MaterialTheme.colorScheme.surface,
                       fontSize = 17.sp,
                       fontWeight = FontWeight.SemiBold,
                       textAlign = TextAlign.Center
                   )
               }
           }else{
               val state by viewModel.state.collectAsState()
               viewModel.getData(fromRealmInstance!!, endRealmInstance!!)

               entries = state.filteredList.size

               if(state.filteredList.isNotEmpty()){
                   state.filteredList.forEach {item->
                       EntryListItem(
                           modifier = Modifier.fillMaxWidth(),
                           idAnalyzer = item,
                           onDeleteClicked = {

                           },
                           shouldShowDeleteIcon = true
                       )
                   }
               }else{
                   Box(
                       modifier = Modifier.fillMaxSize(),
                       contentAlignment = Alignment.Center
                   ) {
                       Text(
                           modifier = Modifier.align(Alignment.Center),
                           text = "No entries have been found for the given time",
                           color = MaterialTheme.colorScheme.surface,
                           fontSize = 17.sp,
                           fontWeight = FontWeight.Normal,
                           textAlign = TextAlign.Center
                       )
                   }
               }
           }
        }
    }
}