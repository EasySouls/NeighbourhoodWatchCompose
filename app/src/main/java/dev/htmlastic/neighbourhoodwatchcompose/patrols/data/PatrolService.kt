package dev.htmlastic.neighbourhoodwatchcompose.patrols.data

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import dev.htmlastic.neighbourhoodwatchcompose.R
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class PatrolService: Service() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val startTime = SystemClock.elapsedRealtimeNanos()
    private val timer = Timer()
    override fun onBind(intent: Intent?): IBinder? {
        // Nothing can bind to this service
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> startForeground()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, "patrol_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Aktív járőrözés")
            .setContentText("Eltelt idő: ")
            .build()

        startForeground(1, notification)

        val task = object : TimerTask() {
            override fun run() {
                val elapsedTime = getElapsedTime()
                val contextText = "Eltelt idő: ${formatElapsedTime(elapsedTime)}"
                updateNotification(contextText)
            }
        }

        // Update notification every second
        timer.schedule(task, 0, 1000)
    }

    private fun getElapsedTime(): Long {
        val currentTIme = SystemClock.elapsedRealtimeNanos()
        val elapsedNanos = currentTIme - startTime
        return TimeUnit.NANOSECONDS.toSeconds(elapsedNanos)
    }

    private fun updateNotification(contentText: String) {
        val builder = NotificationCompat.Builder(this, "patrol_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Aktív járőrözés")
            .setContentText(contentText)
        val notification = builder.build()
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    fun formatElapsedTime(elapsedTime: Long): String {
        val minutes = TimeUnit.SECONDS.toMinutes(elapsedTime)
        val seconds = elapsedTime - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds)
    }

    enum class Actions {
        START,
        STOP
    }
}