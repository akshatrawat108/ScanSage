package com.abhinav.idanalyzer.feature_id_analyzer.presentation.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase.IdAnalyzerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val idAnalyzerUseCase: IdAnalyzerUseCase
): ViewModel(){

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asSharedFlow()

    init {
        getData()
    }


    private fun getData(){
        idAnalyzerUseCase.getEntriesWithoutIdUseCase.invoke().onEach { list->
            _state.value = _state.value.copy(
                listWithoutId =  list
            )
        }.launchIn(viewModelScope)

        idAnalyzerUseCase.getEntriesWithIdUseCase.invoke().onEach {list->
            _state.value = _state.value.copy(
                listWithId = list
            )
        }.launchIn(viewModelScope)
    }

    fun addDatePeriodicWorker(context: Context) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

data class NotificationState(
    val listWithoutId : List<IdAnalyzer> = emptyList(),
    val listWithId : List<IdAnalyzer> = emptyList()
)
