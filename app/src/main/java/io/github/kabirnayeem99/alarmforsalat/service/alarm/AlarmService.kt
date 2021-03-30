package io.github.kabirnayeem99.alarmforsalat.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.kabirnayeem99.alarmforsalat.utils.AlarmReceiver
import io.github.kabirnayeem99.alarmforsalat.utils.Constants

class AlarmService(private val context: Context) {

    companion object {
        private const val TAG = "AlarmService"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Cancels the Alarm
     * @param alarmId [Int], the id of the Salat, such as 1 for Fajr
     */
    fun cancelAlarm(alarmId: Int) {
        alarmManager.cancel(getPendingIntent(getIntent(), alarmId))
    }


    /**
     * Schedules exact alarm or alarm for only once
     * @param timeInMillis [Long]
     * @param alarmId [Int], the id of the salat, such as 1 for Fajr
     */
    fun setExactAlarm(timeInMillis: Long, alarmId: Int) {

        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM, timeInMillis)
                }, alarmId
            )
        )
    }

    /**
     * Schedules repeating alarms on daily basis
     * Repeating basis - 1 Day
     * @param timeInMillis [Long]
     * @param alarmId [Int], the id of Salat, such as 1 for Fajr
     */
    fun setRepetitiveAlarm(timeInMillis: Long, alarmId: Int) {
        Log.d(
            TAG,
            "setRepetitiveAlarm: Setting repetitive alarm for ${Constants.SALATS[alarmId]}"
        )
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM, timeInMillis)
                    putExtra(Constants.EXTRA_SALAT_NAME, Constants.SALATS[alarmId])
                }, alarmId
            )
        )
    }

    /**
     * Sets the alarm
     * @param timeInMillis [Long]
     * @param pendingIntent [PendingIntent]
     */
    private fun setAlarm(
        timeInMillis: Long,
        pendingIntent: PendingIntent,
    ) {
        // this alarm will be allowed to execute
        // even when the system is in low-power idle modes.
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }


    private fun getIntent(
    ): Intent =
        Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent, alarmId: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarmId, //avoiding
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ).also {
            Log.d(TAG, "getPendingIntent: started pending intent for ${intent.action}")
        }
}