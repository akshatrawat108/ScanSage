package com.abhinav.idanalyzer.feature_id_analyzer.presentation.notification

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abhinav.idanalyzer.core.util.DataManager
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import com.abhinav.idanalyzer.feature_id_analyzer.domain.usecase.IdAnalyzerUseCase
import dagger.assisted.AssistedInject
import io.realm.kotlin.types.RealmInstant
import java.time.Duration
import java.time.Instant

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val idAnalyzerUseCase: IdAnalyzerUseCase
): CoroutineWorker(context, workerParameters) {


    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val listIdAnalyzer = mutableListOf<IdAnalyzer>()
        try{
            val flow = idAnalyzerUseCase.getEntriesWithoutIdUseCase.invoke()

            flow.collect{emittedItem->
                listIdAnalyzer.addAll(emittedItem)
            }
            Log.e("NotificationWorker" , "${listIdAnalyzer.size}")
            val item = listIdAnalyzer.firstOrNull()
            if (item != null){
                showNotification(item)
            }

            return Result.Success()
        }catch (e: Exception){
            return Result.Failure()
        }
    }

    private fun showNotification(item: IdAnalyzer){
        val itemInstant = item.date.toInstant()
        val currentInstant = Instant.now()
        val diff = Duration.between(currentInstant, itemInstant).seconds
        Log.e("NotificationWorker" , "diff -> $diff")
        if(diff <= 5){
            val notificationManager = NotificationHelper(context = context)
            notificationManager.createNotificationChannel()
            notificationManager.showNotification(DataManager.getImageFromByteArray(item.imageData))
        }
    }


    private fun RealmInstant.toInstant(): Instant {
        val sec: Long = this.epochSeconds
        val nano: Int = this.nanosecondsOfSecond
        return if (sec >= 0) {
            Instant.ofEpochSecond(sec, nano.toLong())
        } else {
            Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
        }
    }

}
