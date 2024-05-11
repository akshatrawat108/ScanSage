package com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase

import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo
import org.mongodb.kbson.ObjectId

class IdAnalyzerDelete(private val repo: IdAnalyzerRepo) {

    suspend operator fun invoke(id: ObjectId){
        return repo.deleteData(id)
    }

}