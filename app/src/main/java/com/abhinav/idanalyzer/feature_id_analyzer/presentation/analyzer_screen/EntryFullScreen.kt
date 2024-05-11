package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abhinav.idanalyzer.core.util.DataManager
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.toInstant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryFullScreen(
    modifier: Modifier = Modifier,
    idAnalyzer: IdAnalyzer,
    navController: NavController
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Screen",
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        contentColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->

        val imageData = DataManager.getImageFromByteArray(idAnalyzer.imageData)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                model = imageData,
                contentDescription = "image"
            )

            Spacer(modifier = Modifier.height(16.dp))

            val localDateTime = idAnalyzer.date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
            val formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("hh:mm"))
            val formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
            val text = if(idAnalyzer.isPersonWithoutId) "Person does not have any id" else "Person have Id"

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = text,
                color = MaterialTheme.colorScheme.surface,
                fontSize =18.sp,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = "Time : $formattedTime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = "Date : $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}