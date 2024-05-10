package com.abhinav.idanalyzer.feature_id_analyzer.presentation.findings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase.IdAnalyzerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FindingViewModel @Inject constructor(
    private val idAnalyzerUseCase: IdAnalyzerUseCase
): ViewModel() {

    private val _state = MutableStateFlow(Finding())
    val state = _state.asStateFlow()

    fun getData(fromRange: RealmInstant, endRange: RealmInstant) {
       idAnalyzerUseCase.getFilteredEntries.invoke(
           fromRange, endRange
       ).onEach {list->
           _state.value = _state.value.copy(
               filteredList = list
           )
       }.launchIn(viewModelScope)

        Log.e("FindingViewModel" , "filtered list size -> ${_state.value.filteredList.size}")
    }

}

data class Finding(
    val filteredList : List<IdAnalyzer> = emptyList()
)