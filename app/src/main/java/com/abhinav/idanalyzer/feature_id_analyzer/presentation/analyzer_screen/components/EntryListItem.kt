package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.abhinav.idanalyzer.core.util.DataManager
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EntryListItem(
    modifier: Modifier = Modifier,
    idAnalyzer: IdAnalyzer
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Card(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = DataManager.getImageFromByteArray(idAnalyzer.imageData),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop
                )
            }

            val time = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date.from(idAnalyzer.date.toInstant())).uppercase()

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Id card detected",
                    color = Color.Black,
                    fontSize =17.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Time : $time",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
