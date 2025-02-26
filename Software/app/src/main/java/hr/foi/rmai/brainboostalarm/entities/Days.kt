package hr.foi.rmai.brainboostalarm.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_day")
data class Days(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
)
