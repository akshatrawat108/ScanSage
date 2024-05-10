package com.abhinav.idanalyzer.feature_id_analyzer.di

import com.abhinav.idanalyzer.core.util.Constants.APP_ID
import com.abhinav.idanalyzer.feature_id_analyzer.data.IdAnalyzerRepoImpl
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzerRepo
import com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase.IdAnalyzerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module{

    private lateinit var realm: Realm

    @Provides
    @Singleton
    fun providesIdAnalyzerDatabase(): Realm {
        val user = App.create(APP_ID).currentUser
        if(user != null){
            val config = SyncConfiguration.Builder(
                user,
                setOf(IdAnalyzer::class)
            )
                .schemaVersion(3)
                .initialSubscriptions { sub ->
                    add(query = sub.query<IdAnalyzer>(query = "owner_id == $0", ""))
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
        return realm
    }

    @Provides
    @Singleton
    fun providesIdAnalyzerRepo(realm: Realm): IdAnalyzerRepo{
        return IdAnalyzerRepoImpl(realm)
    }

    @Provides
    @Singleton
    fun providesIdAnalyzerUseCase(idAnalyzerRepo: IdAnalyzerRepo): IdAnalyzerUseCase{
        return IdAnalyzerUseCase(idAnalyzerRepo)
    }
}