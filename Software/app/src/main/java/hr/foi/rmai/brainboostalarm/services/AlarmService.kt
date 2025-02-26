package hr.foi.rmai.brainboostalarm.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import hr.foi.rmai.brainboostalarm.MathQuiz
import hr.foi.rmai.brainboostalarm.R
import hr.foi.rmai.brainboostalarm.SnakeGameActivity
import java.util.Random

class AlarmService : Service() {
    private val channelId = "AlarmServiceChannel"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.i("AlarmService", "AlarmService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("AlarmService", "AlarmService started")

        val random = Random()
        val gameIntent: Intent = if (random.nextBoolean()) {
            Intent(this, SnakeGameActivity::class.java)
        } else {
            Intent(this, MathQuiz::class.java)
        }

        gameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(gameIntent)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            gameIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.solve_challenge_title))
            .setContentText(getString(R.string.solve_challenge_text))
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        Log.i("AlarmService", "AlarmService destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
