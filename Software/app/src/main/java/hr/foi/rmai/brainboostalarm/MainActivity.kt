package hr.foi.rmai.brainboostalarm

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.brainboostalarm.adapters.AlarmsAdapter
import hr.foi.rmai.brainboostalarm.database.AlarmsDatabase
import hr.foi.rmai.brainboostalarm.dialogs.AlarmDialog
import hr.foi.rmai.brainboostalarm.dialogs.OnAlarmAddListener
import hr.foi.rmai.brainboostalarm.helpers.AlarmScheduler
import hr.foi.rmai.brainboostalarm.helpers.MockDataLoader
import hr.foi.rmai.brainboostalarm.notification.NotificationHelper

class MainActivity : AppCompatActivity(), OnAlarmAddListener, AlarmsAdapter.OnAlarmsChangedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var alarmsAdapter: AlarmsAdapter
    private lateinit var addNewAlarm: FloatingActionButton
    private lateinit var emoji: ImageView
    private lateinit var noAlarmsText: TextView
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                notificationHelper.createNotificationChannel()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Denied")
                    .setMessage("Notification permission was denied. Unable to send notifications.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNewAlarm = findViewById(R.id.btn_create_new_alarm)
        emoji = findViewById(R.id.img_emoji)
        noAlarmsText = findViewById(R.id.tv_no_alarms_text)
        recyclerView = findViewById(R.id.rv_alarms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        alarmsAdapter = AlarmsAdapter(mutableListOf(), this, this)
        rootView = findViewById(android.R.id.content)
        recyclerView.adapter = alarmsAdapter

        notificationHelper = NotificationHelper(this, requestPermissionLauncher)
        notificationHelper.initialize()

        addNewAlarm.setOnClickListener {
            val dialog = AlarmDialog(this, this)
            dialog.show()
        }

        AlarmsDatabase.buildInstance(applicationContext)
        MockDataLoader.loadMockData()
        checkAlarmExistence()
        loadAlarmList()
    }

    private fun checkAlarmExistence() {
        val hasAlarms = AlarmsDatabase.getInstance().getAlarmsDao().getAllAlarms().isNotEmpty()

        if (hasAlarms) {
            emoji.visibility = ImageView.GONE
            noAlarmsText.visibility = TextView.GONE
        } else {
            emoji.visibility = ImageView.VISIBLE
            noAlarmsText.visibility = TextView.VISIBLE
        }
    }

    override fun onAlarmAdded() {
        loadAlarmList()
    }

    private fun loadAlarmList() {
        val alarms = AlarmsDatabase.getInstance().getAlarmsDao().getAllAlarms()
        alarmsAdapter.updateData(alarms.toMutableList())
        alarms.forEach {
            val days = AlarmsDatabase.getInstance().getAlarmsDayDao().getAlarmsDayById(it.dayId)
            AlarmScheduler.scheduleAlarm(this, it, days)
        }
        checkAlarmExistence()
    }

    override fun onAlarmsChanged() {
        checkAlarmExistence()
    }
}
