package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.EntryListItem
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components.SetUpGraph

@Composable
fun WithoutIdScreen(
    modifier : Modifier = Modifier,
    viewModel : AnalyzerViewModel
){
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
    ) {
        SetUpGraph(viewModel = viewModel, isWithId = false)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = "Recent Entries",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        val listWithoutId = mutableListOf<IdAnalyzer>()

        for (i in 0 until state.listWithoutId.size) {
            state.listWithoutId[i].let {
                listWithoutId.add(it)
            }
            if(i > 6){
                break
            }
        }

        listWithoutId.forEach { item ->
            EntryListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                idAnalyzer = item,
                onDeleteClicked = {
                    viewModel.onEvent(AnalyzerEvent.Delete(item))
                },
                shouldShowDeleteIcon = true
            )
        }
    }
}