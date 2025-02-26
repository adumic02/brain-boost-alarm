package hr.foi.rmai.brainboostalarm.helpers

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import hr.foi.rmai.brainboostalarm.R
import hr.foi.rmai.brainboostalarm.entities.Alarm

object AlarmColorUpdater {
    fun updateColorsBasedOnState(
        alarm: Alarm,
        alarmTime: TextView,
        alarmDaysList: List<TextView>,
        itemView: View
    ) {
        val daysActive = listOf(
            alarm.day.monday,
            alarm.day.tuesday,
            alarm.day.wednesday,
            alarm.day.thursday,
            alarm.day.friday,
            alarm.day.saturday,
            alarm.day.sunday
        )

        alarmTime.setTextColor(
            ContextCompat.getColor(
                itemView.context, when {
                    alarm.active -> R.color.black_gray_color
                    else -> R.color.darker_gray_color
                }
            )
        )

        alarmDaysList.withIndex().forEach { (index, textView) ->
            val isActive = daysActive[index]

            if (isActive && alarm.active) {
                textView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.accent_color
                    )
                )
            } else if (isActive) {
                textView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.darker_gray_color
                    )
                )
            } else {
                textView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white_gray_color
                    )
                )
            }
        }
    }
}