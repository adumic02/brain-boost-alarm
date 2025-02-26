package hr.foi.rmai.brainboostalarm.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import hr.foi.rmai.brainboostalarm.MainActivity
import hr.foi.rmai.brainboostalarm.R
import hr.foi.rmai.brainboostalarm.receivers.NotificationActionReceiver

class NotificationHelper(
    private val context: Context,
    private val requestPermissionLauncher: ActivityResultLauncher<String>? = null
) {

    companion object {
        private const val CHANNEL_ID = "my_channel_id"
        const val ACTION_TURN_OFF_ALARM = "hr.foi.rmai.brainboostalarm.ACTION_TURN_OFF_ALARM"
    }

    fun initialize() {
        checkAndRequestNotificationPermission()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    createNotificationChannel()
                }
                (context as MainActivity).shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    AlertDialog.Builder(context)
                        .setTitle("Notification Permission Needed")
                        .setMessage("This app needs notification permission to send you notifications.")
                        .setPositiveButton("OK") { _, _ ->
                            requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .show()
                }
                else -> {
                    requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            createNotificationChannel()
        }
    }

    fun sendNotification(alarmId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val turnOffIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_TURN_OFF_ALARM
            putExtra("alarm_id", alarmId)
        }
        val turnOffPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, alarmId, turnOffIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setContentTitle("Upcoming alarm")
            .setContentText("Do you want to turn off the upcoming alarm?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(mainPendingIntent)
            .addAction(R.drawable.baseline_alarm_24, "Turn Off", turnOffPendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(alarmId, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel for my app notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
