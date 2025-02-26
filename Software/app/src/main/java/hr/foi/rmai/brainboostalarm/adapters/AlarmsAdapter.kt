package hr.foi.rmai.brainboostalarm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import hr.foi.rmai.brainboostalarm.R
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.dialogs.AlarmDialog
import hr.foi.rmai.brainboostalarm.dialogs.OnAlarmAddListener
import hr.foi.rmai.brainboostalarm.entities.Alarm
import hr.foi.rmai.brainboostalarm.helpers.AlarmColorUpdater
import hr.foi.rmai.brainboostalarm.helpers.AlarmScheduler

class AlarmsAdapter(
    private var alarmsList: MutableList<Alarm>,
    private val onAlarmAddListener: OnAlarmAddListener,
    private val onAlarmsChangedListener: OnAlarmsChangedListener
) : RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    interface OnAlarmsChangedListener {
        fun onAlarmsChanged()
    }

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val alarmTime: TextView
        private val alarmActive: SwitchMaterial
        private val alarmDaysList: List<TextView>

        init {
            alarmTime = view.findViewById(R.id.tv_clock)
            alarmDaysList = listOf(
                view.findViewById(R.id.tv_monday),
                view.findViewById(R.id.tv_tuesday),
                view.findViewById(R.id.tv_wednesday),
                view.findViewById(R.id.tv_thursday),
                view.findViewById(R.id.tv_friday),
                view.findViewById(R.id.tv_saturday),
                view.findViewById(R.id.tv_sunday)
            )
            alarmActive = view.findViewById(R.id.alarm_switch)

            view.setOnLongClickListener {
                val currentAlarm = alarmsList[adapterPosition]

                val alertDialogBuilder =
                    AlertDialog.Builder(view.context).setNegativeButton("Delete alarm") { _, _ ->
                        val alarmsDao = AlarmsDatabase.getInstance().getAlarmsDao()
                        alarmsDao.removeAlarm(currentAlarm)
                        removeAlarmFromList()
                        onAlarmsChangedListener.onAlarmsChanged()
                    }.setNeutralButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                alertDialogBuilder.show()
                true
            }

            view.setOnClickListener {
                val currentAlarm = alarmsList[adapterPosition]
                val dialog = AlarmDialog(view.context, onAlarmAddListener, currentAlarm)
                dialog.show()
            }

            alarmActive.setOnCheckedChangeListener { _, isChecked ->
                val currentAlarm = alarmsList[adapterPosition]
                currentAlarm.active = isChecked
                val alarmDao = AlarmsDatabase.getInstance().getAlarmsDao()
                alarmDao.updateAlarmActive(currentAlarm.id.toInt(), isChecked)

                if (isChecked) {
                    val days = AlarmsDatabase.getInstance().getAlarmsDayDao()
                        .getAlarmsDayById(currentAlarm.dayId)
                    AlarmScheduler.scheduleAlarm(view.context, currentAlarm, days)
                } else {
                    val days = AlarmsDatabase.getInstance().getAlarmsDayDao().getAlarmsDayById(currentAlarm.dayId)
                    AlarmScheduler.cancelAlarm(view.context, currentAlarm, days)
                }

                AlarmColorUpdater.updateColorsBasedOnState(
                    currentAlarm,
                    alarmTime,
                    alarmDaysList,
                    itemView
                )
                onAlarmsChangedListener.onAlarmsChanged()
            }
        }

        private fun removeAlarmFromList() {
            alarmsList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }

        fun bind(alarm: Alarm) {
            alarmTime.text = alarm.time
            alarmActive.isChecked = alarm.active
            alarmDaysList[0].text = "M"
            alarmDaysList[1].text = "T"
            alarmDaysList[2].text = "W"
            alarmDaysList[3].text = "T"
            alarmDaysList[4].text = "F"
            alarmDaysList[5].text = "S"
            alarmDaysList[6].text = "S"

            AlarmColorUpdater.updateColorsBasedOnState(
                alarm,
                alarmTime,
                alarmDaysList,
                itemView
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val alarmView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.alarm_list_item, parent, false)
        return AlarmViewHolder(alarmView)
    }

    override fun getItemCount(): Int = alarmsList.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarmsList[position])
    }

    fun updateData(newAlarmsList: MutableList<Alarm>) {
        this.alarmsList = newAlarmsList
        notifyDataSetChanged()
        onAlarmsChangedListener.onAlarmsChanged()
    }
}
