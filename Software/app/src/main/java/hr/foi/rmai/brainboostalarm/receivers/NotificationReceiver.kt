package hr.foi.rmai.brainboostalarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.notification.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("alarm_id", -1)
        if (alarmId != -1) {
            val alarm = AlarmsDatabase.getInstance().getAlarmsDao().getAlarm(alarmId)
            if (alarm.active) {
                val notificationHelper = NotificationHelper(context)
                notificationHelper.sendNotification(alarmId)
            }
        }
    }
}
