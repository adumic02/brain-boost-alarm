package hr.foi.rmai.brainboostalarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.rmai.brainboostalarm.entities.Alarm
import hr.foi.rmai.brainboostalarm.entities.Days

@Database(version = 1, entities = [Alarm::class, Days::class], exportSchema = false)
abstract class AlarmsDatabase : RoomDatabase() {

    abstract fun getAlarmsDao(): AlarmsDAO
    abstract fun getAlarmsDayDao(): AlarmsDayDAO

    companion object {
        private var implementedInstance: AlarmsDatabase? = null

        fun getInstance(): AlarmsDatabase {
            if (implementedInstance == null) {
                throw NullPointerException("Database instance has not yet been created!")
            }
            return implementedInstance!!
        }

        fun buildInstance(context: Context) {
            if (implementedInstance == null) {
                val instanceBuilder = Room.databaseBuilder(
                    context,
                    AlarmsDatabase::class.java,
                    "alarms.db"
                )
                instanceBuilder.fallbackToDestructiveMigration()
                instanceBuilder.allowMainThreadQueries()

                implementedInstance = instanceBuilder.build()
            }
        }
    }
}