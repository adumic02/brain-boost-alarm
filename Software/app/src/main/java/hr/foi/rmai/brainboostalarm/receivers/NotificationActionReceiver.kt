package hr.foi.rmai.brainboostalarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import hr.foi.rmai.brainboostalarm.notification.NotificationHelper
import hr.foi.rmai.brainboostalarm.services.AlarmService

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val alarmId = intent.getIntExtra("alarm_id", -1)

        if (action == NotificationHelper.ACTION_TURN_OFF_ALARM && alarmId != -1) {
            Log.i("NotificationActionReceiver", "Turn off alarm action received for alarm ID: $alarmId")

            val serviceIntent = Intent(context, AlarmService::class.java)
            context.stopService(serviceIntent)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.cancel(alarmId)
        }
    }
}
