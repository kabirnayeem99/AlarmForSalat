package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.kabirnayeem99.alarmforsalat.service.alarm.AlarmService
import io.karn.notify.Notify
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * This broadcast receiver (receiver), which is an Android component
 * which allows  to register for Alarm events.
 * All registered receivers for this event gives a notification by the Android
 * runtime once this event happens or alarm time comes.
 */
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmReceiver"
    }

    /**
     * Triggers once the event happens
     */
    override fun onReceive(context: Context, intent: Intent) {

        // gets the time through intent extras
        val timeInMillis: Long = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM, 0)

        // sets event based on the action
        when (intent.action) {
            Constants.SET_EXACT_ALARM -> {
                buildNotifications(context, "Salat time", convertDate(timeInMillis))
            }
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(AlarmService(context))
                buildNotifications(
                    context,
                    "Set Repetitive Exact Time",
                    convertDate(timeInMillis)
                )
            }
            else -> {
                println("Nothing happened")
            }
        }


    }


    /**
     * sets repetitive alarm by 1 day
     */
    private fun setRepetitiveAlarm(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(1)
            Log.d(TAG, "Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis, 0)
    }

    /**
     * Creates notification to show on even receive
     */
    private fun buildNotifications(context: Context, title: String, salatName: String) {
        Notify.with(context).content {
            this.title = title
            this.text = salatName
        }
            .show()
    }

    /**
     * Converts the milli second time into string
     */
    private fun convertDate(timeMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return formatter.format(Date(timeMillis))
    }
}