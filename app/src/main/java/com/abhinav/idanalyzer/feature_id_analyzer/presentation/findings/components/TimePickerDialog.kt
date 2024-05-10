package com.abhinav.idanalyzer.feature_id_analyzer.presentation.findings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier,
    onDatePicked: (LocalDate) -> Unit,
    onTimePicked: (LocalTime) -> Unit,
    count: String
) {

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }

    var hasTimeSelected by remember {
        mutableStateOf(false)
    }

    var hasDateSelected by remember {
        mutableStateOf(false)
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()



   Box(
       modifier = modifier.fillMaxWidth(),
   ){
       Text(
           modifier = Modifier
               .padding(16.dp)
               .align(Alignment.CenterStart),
           text = "Pick $count time",
           color = MaterialTheme.colorScheme.surface,
           fontSize = 16.sp,
           fontWeight = FontWeight.Normal,
           style = MaterialTheme.typography.bodyMedium
       )
       Spacer(modifier = Modifier.width(16.dp))
       Row(
           modifier = Modifier.align(Alignment.CenterEnd)
       ){
           TextButton(
               modifier = Modifier,
               onClick = {
                   timeDialogState.show()
                   hasTimeSelected = true
               },
               colors = ButtonDefaults.textButtonColors(
                   containerColor = MaterialTheme.colorScheme.secondary
               )
           ) {
               val text = if(hasTimeSelected) formattedTime else "Pick a time"

               Text(
                   modifier = Modifier,
                   text = text,
                   color = MaterialTheme.colorScheme.surface,
                   fontSize = 16.sp,
                   fontWeight = FontWeight.Normal,
                   style = MaterialTheme.typography.bodyMedium
               )
           }
           Spacer(modifier = Modifier.width(8.dp))
           TextButton(
               modifier = Modifier,
               onClick = {
                   dateDialogState.show()
                   hasDateSelected = true
               },
               colors = ButtonDefaults.textButtonColors(
                   containerColor = MaterialTheme.colorScheme.secondary
               )
           ) {

               val text = if(hasDateSelected) formattedDate else "Pick a date"

               Text(
                   modifier = Modifier,
                   text = text,
                   color = MaterialTheme.colorScheme.surface,
                   fontSize = 16.sp,
                   fontWeight = FontWeight.Normal,
                   style = MaterialTheme.typography.bodyMedium
               )
           }
       }

       MaterialDialog(
           dialogState = dateDialogState,
           buttons = {
               positiveButton(text = "Ok"){
                   onDatePicked(pickedDate)
               }
               negativeButton(text = "Cancel")
           }
       ) {
           datepicker(
               initialDate = LocalDate.now(),
               title = "Pick a date",
           ){
               pickedDate = it
           }
       }

       MaterialDialog(
           dialogState = timeDialogState,
           buttons = {
               positiveButton(text = "Ok"){
                   onTimePicked(pickedTime)
               }
               negativeButton(text = "Cancel")
           }
       ) {
           timepicker(
               initialTime = LocalTime.NOON,
               title = "Pick a time",
               timeRange = LocalTime.MIDNIGHT..LocalTime.NOON,
               is24HourClock = true
           ){
               pickedTime = it
           }
       }
   }

}