package com.abhinav.idanalyzer.feature_id_analyzer.presentation.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abhinav.idanalyzer.R

class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    @SuppressLint("MissingPermission", "NotificationPermission")
    fun showNotification( bitmap: Bitmap) {

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Suspect detected")
            .setContentText("Person without ID detected")
            .setSmallIcon(R.drawable.google_logo)
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun createNotificationChannel() {
        val name = "Notification"
        val descriptionText = "Id notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    companion object {
        private const val CHANNEL_ID = "my_channel_id"
        private const val notificationId = 123
    }
}
