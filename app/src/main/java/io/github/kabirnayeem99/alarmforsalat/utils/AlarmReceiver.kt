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

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: started alarm receiver")
        intent.let {

            val timeInMillis: Long = it.getLongExtra(Constants.EXTRA_EXACT_ALARM, 0)
            when (it.action) {
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


    }


    private fun setRepetitiveAlarm(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(1)
            Log.d(TAG, "Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis, 0)
    }

    private fun buildNotifications(context: Context, title: String, salatName: String) {
        Notify.with(context).content {
            this.title = title
            this.text = salatName
        }
            .show()
    }

    private fun convertDate(timeMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return formatter.format(Date(timeMillis))
    }
}