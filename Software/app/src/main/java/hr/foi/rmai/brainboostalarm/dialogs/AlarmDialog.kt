package hr.foi.rmai.brainboostalarm.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import hr.foi.rmai.brainboostalarm.R
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.entities.Alarm
import hr.foi.rmai.brainboostalarm.entities.Days
import hr.foi.rmai.brainboostalarm.view.DayWithDotView
import java.util.Calendar


interface OnAlarmAddListener {
    fun onAlarmAdded()
}

class AlarmDialog(
    context: Context,
    private val onAlarmAddListener: OnAlarmAddListener,
    private val alarm: Alarm? = null
) :
    Dialog(context) {

    private var alarmRingTime: TextView
    private lateinit var dayMonday: DayWithDotView
    private lateinit var dayTuesday: DayWithDotView
    private lateinit var dayWednesday: DayWithDotView
    private lateinit var dayThursday: DayWithDotView
    private lateinit var dayFriday: DayWithDotView
    private lateinit var daySaturday: DayWithDotView
    private lateinit var daySunday: DayWithDotView

    init {
        setContentView(R.layout.new_alarm_dialog)
        dayMonday = findViewById(R.id.tv_monday)
        dayTuesday = findViewById(R.id.tv_tuesday)
        dayWednesday = findViewById(R.id.tv_wednesday)
        dayThursday = findViewById(R.id.tv_thursday)
        dayFriday = findViewById(R.id.tv_friday)
        daySaturday = findViewById(R.id.tv_saturday)
        daySunday = findViewById(R.id.tv_sunday)

        dayMonday.setDayText("Mon")
        dayTuesday.setDayText("Tue")
        dayWednesday.setDayText("Wed")
        dayThursday.setDayText("Thu")
        dayFriday.setDayText("Fri")
        daySaturday.setDayText("Sat")
        daySunday.setDayText("Sun")

        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(getDrawable(context, R.drawable.new_alarm_dialog_box_design))

        val btnCancel = findViewById<Button>(R.id.btn_cancel_new_alarm)
        val btnSave = findViewById<Button>(R.id.btn_save_new_alarm)
        alarmRingTime = findViewById(R.id.tv_new_alarm_dialog_time)
        alarmRingTime.setOnClickListener {
            showTimePicker(context, alarmRingTime)
        }

        btnSave.setOnClickListener {
            if (alarm != null) {
                updateAlarmInDatabase(alarm)
            } else {
                saveAlarmToDatabase()
            }
            onAlarmAddListener.onAlarmAdded()
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        alarm?.let {
            populateDialogWithAlarmData(it)
        }
    }

    private fun populateDialogWithAlarmData(alarm: Alarm) {
        alarmRingTime.text = alarm.time
        val days = AlarmsDatabase.getInstance().getAlarmsDayDao().getAlarmsDayById(alarm.dayId)
        dayMonday.isDaySelected = days.monday
        dayTuesday.isDaySelected = days.tuesday
        dayWednesday.isDaySelected = days.wednesday
        dayThursday.isDaySelected = days.thursday
        dayFriday.isDaySelected = days.friday
        daySaturday.isDaySelected = days.saturday
        daySunday.isDaySelected = days.sunday
    }

    fun showTimePicker(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                textView.text = formattedTime
            },
            initialHour,
            initialMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun saveAlarmToDatabase() {
        val selectedAlarmTime = alarmRingTime.text.toString()

        val daysEntity = Days(
            id = 0,
            monday = dayMonday.isDaySelected,
            tuesday = dayTuesday.isDaySelected,
            wednesday = dayWednesday.isDaySelected,
            thursday = dayThursday.isDaySelected,
            friday = dayFriday.isDaySelected,
            saturday = daySaturday.isDaySelected,
            sunday = daySunday.isDaySelected
        )

        val alarmDaysDao = AlarmsDatabase.getInstance().getAlarmsDayDao()
        val dayID = alarmDaysDao.insertDays(daysEntity)[0]

        val newAlarm = Alarm(
            id = 0,
            active = true,
            dayId = dayID,
            time = selectedAlarmTime
        )
        AlarmsDatabase.getInstance().getAlarmsDao().insertAlarm(newAlarm)
        Toast.makeText(context, "New alarm set for $selectedAlarmTime", Toast.LENGTH_SHORT)
            .show()
    }

    private fun updateAlarmInDatabase(alarm: Alarm) {
        val selectedAlarmTime = alarmRingTime.text.toString()

        val daysEntity = Days(
            id = alarm.dayId,
            monday = dayMonday.isDaySelected,
            tuesday = dayTuesday.isDaySelected,
            wednesday = dayWednesday.isDaySelected,
            thursday = dayThursday.isDaySelected,
            friday = dayFriday.isDaySelected,
            saturday = daySaturday.isDaySelected,
            sunday = daySunday.isDaySelected
        )

        val alarmDaysDao = AlarmsDatabase.getInstance().getAlarmsDayDao()
        alarmDaysDao.updateDays(daysEntity)

        alarm.time = selectedAlarmTime
        AlarmsDatabase.getInstance().getAlarmsDao().updateAlarm(alarm)
        Toast.makeText(context, "Alarm updated to $selectedAlarmTime", Toast.LENGTH_SHORT)
            .show()
    }
}