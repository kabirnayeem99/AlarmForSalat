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

    fun cancelAlarm(alarmId: Int) {
        alarmManager.cancel(
            getPendingIntent(
                getIntent(

                ), alarmId
            )
        )
    }


    fun setExactAlarm(timeInMillis: Long, alarmId: Int) {

        Log.d(TAG, "setExactAlarm: setting up alarm for $timeInMillis")
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM, timeInMillis)
                    Log.d(TAG, "setExactAlarm: setting up $action for $timeInMillis")
                }, alarmId
            )
        )
    }

    //1 day
    fun setRepetitiveAlarm(timeInMillis: Long, alarmId: Int) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM, timeInMillis)
                }, alarmId
            )
        )
    }

    private fun setAlarm(
        timeInMillis: Long,
        pendingIntent: PendingIntent,
    ) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }


    private fun getIntent(
    ): Intent =
        Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent, alarmId: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarmId + System.currentTimeMillis().toInt(), //avoiding
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ).also {
            Log.d(TAG, "getPendingIntent: started pending intent for ${intent.action}")
        }
}