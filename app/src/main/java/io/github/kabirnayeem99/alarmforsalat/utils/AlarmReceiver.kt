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
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time one can use the other methods on
     * BroadcastReceiver to view/modify the current result values.
     *
     * This onReceive() is used to show notification on intent receive
     * and in case of repetitive alarm, setting the alarm for next salat.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent) {

        // gets the time through intent extras
        val timeInMillis: Long = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM, 0)
        val salatName: String? = intent.getStringExtra(Constants.EXTRA_SALAT_NAME)

        // sets event based on the action
        when (intent.action) {
            Constants.SET_EXACT_ALARM -> {
                val cal = Calendar.getInstance()

                // avoids showing notification for previous alarms
            }
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                val cal = Calendar.getInstance()
                if (cal.timeInMillis >= timeInMillis) {
                    if (salatName != null) {
                        buildNotifications(context, salatName, convertDate(timeInMillis))
                    }
                }
                if (cal.timeInMillis >= timeInMillis) {
                    setRepetitiveAlarm(AlarmService(context), timeInMillis)
                    Log.d(
                        TAG,
                        "onReceive: current time is ${convertDate(cal.timeInMillis)} alarm time is " +
                                convertDate(timeInMillis)
                    )
                    if (salatName != null) {
                        buildNotifications(
                            context,
                            salatName,
                            convertDate(timeInMillis)
                        )
                    }
                }
            }
            else -> {
                println("Nothing happened")
            }
        }
    }


    /**
     * Schedules repeating alarms on daily basis
     * Repeating basis - 1 Day
     * @param alarmService [AlarmService]
     */
    private fun setRepetitiveAlarm(alarmService: AlarmService, timeMillis: Long) {
        val timeInMillis = timeMillis + TimeUnit.DAYS.toMillis(1)
        Log.d(TAG, "Set alarm for next day same time - ${convertDate(timeInMillis)}")
        alarmService.setRepetitiveAlarm(timeInMillis, 0)
    }

    /**
     * Creates notification to show on event receive
     * @param context [Context]
     * @param title [String]
     * @param salatName [String]
     */
    private fun buildNotifications(context: Context, title: String, salatName: String) {
        Notify.with(context).content {
            this.title = title
            this.text = salatName
        }
            .show()
    }

    /**
     * Converts the milli second time into formatted string date
     * @param timeMillis [Long]
     */
    private fun convertDate(timeMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return formatter.format(Date(timeMillis))
    }
}