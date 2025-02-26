package hr.foi.rmai.brainboostalarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hr.foi.rmai.brainboostalarm.entities.Days

@Dao
interface AlarmsDayDAO {

    @Query("SELECT * FROM alarm_day")
    fun getAllDays(): List<Days>

    @Query("SELECT * FROM alarm_day WHERE id = :id")
    fun getAlarmsDayById(id: Long): Days

    @Insert
    fun insertDays(vararg alarm: Days): List<Long>

    @Update
    fun updateDays(vararg days: Days)
}