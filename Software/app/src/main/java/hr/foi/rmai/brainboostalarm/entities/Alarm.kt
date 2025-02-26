package hr.foi.rmai.brainboostalarm.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase

@Entity(
    tableName = "alarm",
    foreignKeys = [
        ForeignKey(
            entity = Days::class,
            parentColumns = ["id"],
            childColumns = ["day_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)

data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "time", index = true) var time: String,
    @ColumnInfo(name = "day_id", index = true) var dayId: Long,
    @ColumnInfo(name = "active", index = true) var active: Boolean
) {
    @delegate: Ignore
    val day: Days by lazy {
        AlarmsDatabase.getInstance().getAlarmsDayDao().getAlarmsDayById(dayId)
    }
}