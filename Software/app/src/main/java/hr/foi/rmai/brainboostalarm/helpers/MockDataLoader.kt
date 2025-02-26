package hr.foi.rmai.brainboostalarm.helpers

import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.entities.Alarm
import hr.foi.rmai.brainboostalarm.entities.Days

object MockDataLoader {
    fun loadMockData() {
        val alarmDao = AlarmsDatabase.getInstance().getAlarmsDao()
        val alarmDaysDao = AlarmsDatabase.getInstance().getAlarmsDayDao()

        if (alarmDao.getActiveAlarms(false).isEmpty()
            && alarmDao.getActiveAlarms(true).isEmpty()
            && alarmDaysDao.getAllDays().isEmpty()

        ) {
            val days = arrayOf(
                Days(1, true, true, true, false, false, false, false),
                Days(2, false, false, false, true, true, false, false),
                Days(3, false, false, false, false, false, true, true),
            )
            alarmDaysDao.insertDays(*days)

            val dbDays = alarmDaysDao.getAllDays()

            val alarm = arrayOf(
                Alarm(1, "10:10", dbDays[1].id, false),
                Alarm(2, "08:20", dbDays[0].id, true),
                Alarm(3, "07:45", dbDays[2].id, false),
            )
            alarmDao.insertAlarm(*alarm)
        }
    }
}