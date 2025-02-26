package hr.foi.rmai.brainboostalarm.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hr.foi.rmai.brainboostalarm.entities.Alarm

@Dao
interface AlarmsDAO {
    @Query("SELECT * FROM alarm WHERE id = :id")
    fun getAlarm(id: Int): Alarm

    @Query("SELECT * FROM alarm")
    fun getAllAlarms(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE active  = :active")
    fun getActiveAlarms(active: Boolean): List<Alarm>

    @Query("UPDATE alarm SET active = :active WHERE id = :id")
    fun updateAlarmActive(id: Int, active: Boolean)

    @Insert
    fun insertAlarm(vararg alarm: Alarm): List<Long>

    @Delete
    fun removeAlarm(vararg task: Alarm)

    @Update
    fun updateAlarm(vararg task: Alarm)
}