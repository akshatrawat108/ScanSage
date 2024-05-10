package com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase

import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFilteredEntries(private val repo: IdAnalyzerRepo) {

    operator fun invoke(fromRange: RealmInstant, endRange: RealmInstant): Flow<List<IdAnalyzer>> {
        return repo.getData().map {
            it.filter { entry -> entry.date in (fromRange..endRange)  }
        }
    }

}