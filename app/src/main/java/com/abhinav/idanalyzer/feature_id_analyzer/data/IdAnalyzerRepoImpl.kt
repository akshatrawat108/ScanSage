package com.abhinav.idanalyzer.feature_id_analyzer.data

import android.util.Log
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class IdAnalyzerRepoImpl constructor(
    private val realm: Realm
): IdAnalyzerRepo {

    override fun getData(): Flow<List<IdAnalyzer>> {
        return realm.query<IdAnalyzer>().asFlow().map { it.list }
    }

    override fun getEntriesWithId(): Flow<List<IdAnalyzer>> {
        return realm.query<IdAnalyzer>("isPersonWithoutId = $0" , false).asFlow().map{ it.list }
    }

    override fun getEntriesWithoutId(): Flow<List<IdAnalyzer>> {
        return realm.query<IdAnalyzer>("isPersonWithoutId = $0" , true).asFlow().map{ it.list }
    }

    override suspend fun addData(list: List<IdAnalyzer>) {
        realm.write {
            try{
                list.forEach {idAnalyzer ->
                    idAnalyzer.owner_id = ""
                    copyToRealm(idAnalyzer)
                }
            }catch (e: Exception){
                Log.d("MongoDbRepo" , e.message.toString())
            }
        }
    }

    override suspend fun deleteData(id: ObjectId) {
        realm.write {
            try {
                val idAnalyzer = query<IdAnalyzer>(query = "_id == $0", id)
                    .first()
                    .find()
                idAnalyzer?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongobRepositoryImpl", "${e.message}")
            }
        }
    }
}