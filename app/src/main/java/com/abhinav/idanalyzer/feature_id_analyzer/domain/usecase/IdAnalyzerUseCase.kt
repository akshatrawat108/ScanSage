package com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase

import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo

class IdAnalyzerUseCase(repo: IdAnalyzerRepo){
    val getEntriesWithIdUseCase = GetEntriesWithId(repo)
    val getEntriesWithoutIdUseCase = GetEntriesWithoutId(repo)
    val getFilteredEntries = GetFilteredEntries(repo)
}