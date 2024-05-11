package com.abhinav.idanalyzer.feature_id_analyzer.domain

import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface IdAnalyzerRepo {
    fun getData() : Flow<List<IdAnalyzer>>
    fun getEntriesWithId() : Flow<List<IdAnalyzer>>
    fun getEntriesWithoutId() : Flow<List<IdAnalyzer>>
    suspend fun addData(list: List<IdAnalyzer>)

    suspend fun deleteData(id: ObjectId)
}