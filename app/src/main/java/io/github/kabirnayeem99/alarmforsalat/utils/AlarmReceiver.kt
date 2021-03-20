package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.karn.notify.Notify
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started alarm receiver")
        intent?.let {
            val timeInMillis: Long = it.getLongExtra(Constants.EXTRA_EXACT_ALARM, 0)
            when (it.action) {
                Constants.SET_EXACT_ALARM -> {
                    context?.let { ctxt ->
                        buildNotifications(ctxt, "Salat time", convertDate(timeInMillis))
                    }
                }
                else -> {
                    println("Nothing happened")
                }
            }
        }


    }

    private fun buildNotifications(context: Context, title: String, salatName: String) {
        Notify.with(context).content {
            this.title = title
            this.text = salatName
        }.show()
    }

    private fun convertDate(timeMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(timeMillis))
    }
}