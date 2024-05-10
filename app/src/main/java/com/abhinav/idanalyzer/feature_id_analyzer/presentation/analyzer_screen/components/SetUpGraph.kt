package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.AnalyzerViewModel
import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.ZoneId

@Composable
fun SetUpGraph(
    modifier: Modifier = Modifier,
    viewModel: AnalyzerViewModel,
    isWithId : Boolean
) {
    val state by viewModel.state.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        val list = if(isWithId) state.listWithId else state.listWithoutId

        if (list.isNotEmpty()) {
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 8.dp),
               horizontalAlignment = Alignment.CenterHorizontally
           ){
               Graph(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(300.dp)
                       .background(MaterialTheme.colorScheme.secondary),
                   list = list
               )
               Spacer(modifier = Modifier.height(8.dp))
               CountComposable(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 16.dp),
                   count = list.size.toString()
               )
           }

            Log.e("Graph" , "data size : -> ${state.listWithId.size}")

        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Not Enough data to show list",
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun Graph(
    modifier: Modifier = Modifier,
    list: List<IdAnalyzer>,
) {
    val mapOfEntries = getListOfTime(list)
    val pointsData = mutableListOf<Point>()

    var highestNumberOfPeople = Int.MIN_VALUE
    var lowestNumberOfPeople = Int.MAX_VALUE
    var highestHourOfEntry = Int.MIN_VALUE
    var lowestHourOfEntry = Int.MAX_VALUE

    mapOfEntries.forEach { (key, value) ->
        pointsData.add(Point(key.toFloat(), value.toFloat()))
        highestNumberOfPeople = highestNumberOfPeople.coerceAtLeast(value)
        lowestNumberOfPeople = lowestNumberOfPeople.coerceAtMost(value)
        lowestHourOfEntry = lowestHourOfEntry.coerceAtMost(key)
        highestHourOfEntry = highestHourOfEntry.coerceAtLeast(key)
    }

   // val pseudoPointsData = mutableListOf<Point>()
    //  pseudoPointsData.add(Point(0f, 6f))
    //  pseudoPointsData.add(Point(1f, 2f))
    //  pseudoPointsData.add(Point(2f, 3f))
    //  pseudoPointsData.add(Point(3f, 8f))
    //  pseudoPointsData.add(Point(4f, 3f))
    //  pseudoPointsData.add(Point(5f, 3f))
    //  pseudoPointsData.add(Point(6f, 7f))
    //  pseudoPointsData.add(Point(10f, 7f))
    // pseudoPointsData.add(Point(12f, 9f))
    // pseudoPointsData.add(Point(14f, 2f))

    //  println("pseudoPointsData -> $pseudoPointsData")

    val xAxisData = AxisData.Builder()
        .axisStepSize(70.dp)
        .backgroundColor(MaterialTheme.colorScheme.secondary)
        .steps(24 - lowestHourOfEntry)
        //.steps(24)
        .labelAndAxisLinePadding(15.dp)
        .labelData {it->
            val time = it + lowestHourOfEntry
            if(time <= 12){
                return@labelData "$time AM"
            }else{
                return@labelData "${time - 12} PM"
            }

        }
        .axisLineColor(MaterialTheme.colorScheme.surface)
        .axisLabelColor(MaterialTheme.colorScheme.surface)
        .axisLineThickness(3.dp)
        .shouldDrawAxisLineTillEnd(false)
        .axisLabelDescription {
            "Time(in hours)"
        }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(2)
        .backgroundColor(MaterialTheme.colorScheme.secondary)
        .labelAndAxisLinePadding(15.dp)
        .labelData {
            when (it) {
                0 -> return@labelData lowestNumberOfPeople.toString()
                2 -> return@labelData highestNumberOfPeople.toString()
                else -> ""
            }
        }
        .axisLineColor(MaterialTheme.colorScheme.surface)
        .axisLabelColor(MaterialTheme.colorScheme.surface)
        .axisLineThickness(3.dp)
        .axisLabelDescription {
            "Number of people"
        }
        .build()

    val lineCharData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.surface,
                        lineType = LineType.SmoothCurve(isDotted = false),
                        width = 5f
                    ),
                    IntersectionPoint(MaterialTheme.colorScheme.secondary),
                    SelectionHighlightPoint(
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        isHighlightLineRequired = false
                    ),
                    ShadowUnderLine(
                        alpha = 0.7f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.secondary,
        isZoomAllowed = true,
        paddingRight = 0.dp,
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    LineChart(
        modifier = modifier,
        lineChartData = lineCharData
    )
}

/*
    returns a map which stores the number of people entered with respect to time of their entry(hour).
    Map<hour, number of people>
*/
fun getListOfTime(
    list: List<IdAnalyzer>,
): Map<Int, Int> {
    val map = mutableMapOf<Int, Int>()

    list.forEach { idAnalyzer ->
        val localDateTime = idAnalyzer.date.toInstant().atZone(ZoneId.systemDefault())
        map[localDateTime.hour] = map.getOrDefault(localDateTime.hour, 0) + 1
    }
    return map
}


fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

