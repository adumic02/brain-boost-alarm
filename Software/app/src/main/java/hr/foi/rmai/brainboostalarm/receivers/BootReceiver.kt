package hr.foi.rmai.brainboostalarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.foi.rmai.brainboostalarm.helpers.AlarmScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmScheduler.rescheduleAlarms(context)
        }
    }
}
