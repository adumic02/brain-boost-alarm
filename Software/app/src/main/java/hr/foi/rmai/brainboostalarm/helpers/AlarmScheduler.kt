package hr.foi.rmai.brainboostalarm.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.entities.Alarm
import hr.foi.rmai.brainboostalarm.entities.Days
import hr.foi.rmai.brainboostalarm.receivers.AlarmReceiver
import hr.foi.rmai.brainboostalarm.receivers.NotificationReceiver
import java.util.Calendar

object AlarmScheduler {
    fun scheduleAlarm(context: Context, alarm: Alarm, days: Days) {
        if (!alarm.active) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id.toInt())
        }
        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id.toInt())
        }

        val timeParts = alarm.time.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        val daysOfWeek = arrayOf(
            days.sunday, days.monday, days.tuesday, days.wednesday,
            days.thursday, days.friday, days.saturday
        )

        for (i in daysOfWeek.indices) {
            if (daysOfWeek[i]) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.DAY_OF_WEEK, (i + 1) % 7)

                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.WEEK_OF_YEAR, 1)
                    }
                }

                val notificationTime = calendar.timeInMillis - 3600000

                Log.i("AlarmScheduler", "Setting alarm for day ${calendar.get(Calendar.DAY_OF_WEEK)} at ${calendar.time}")

                val alarmPendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id.toInt() * 10 + i,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notificationPendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id.toInt() * 10 + i + 1000,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    notificationPendingIntent
                )

                Log.i("AlarmScheduler", "Notification scheduled for alarm ID: ${alarm.id} at $notificationTime")
            }
        }
    }

    fun cancelAlarm(context: Context, alarm: Alarm, days: Days) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id.toInt())
        }
        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id.toInt())
        }

        val daysOfWeek = arrayOf(
            days.sunday, days.monday, days.tuesday, days.wednesday,
            days.thursday, days.friday, days.saturday
        )

        for (i in daysOfWeek.indices) {
            if (daysOfWeek[i]) {
                val alarmPendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id.toInt() * 10 + i,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notificationPendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id.toInt() * 10 + i + 1000,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.cancel(alarmPendingIntent)
                alarmManager.cancel(notificationPendingIntent)

                Log.i("AlarmScheduler", "Canceled alarm and notification for alarm ID: ${alarm.id}")
            }
        }
    }

    fun rescheduleAlarms(context: Context) {
        val alarms = AlarmsDatabase.getInstance().getAlarmsDao().getAllAlarms()
        for (alarm in alarms) {
            if (alarm.active) {
                val days = AlarmsDatabase.getInstance().getAlarmsDayDao().getAlarmsDayById(alarm.dayId)
                scheduleAlarm(context, alarm, days)
            }
        }
    }
}
