package dev.htmlastic.neighbourhoodwatchcompose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NeighbourhoodWatchApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "patrol_channel",
            "Patrol Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}