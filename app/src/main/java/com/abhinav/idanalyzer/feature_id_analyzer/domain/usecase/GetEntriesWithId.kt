package com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase

import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEntriesWithId(private val repo: IdAnalyzerRepo){

    operator fun invoke(): Flow<List<IdAnalyzer>> {
        return repo.getEntriesWithId().map {list->
            list.sortedByDescending { it.date }
        }
    }

}