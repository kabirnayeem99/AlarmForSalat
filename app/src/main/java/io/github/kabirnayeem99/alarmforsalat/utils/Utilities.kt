package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import io.github.kabirnayeem99.alarmforsalat.enum.Meridiem
import io.github.kabirnayeem99.alarmforsalat.service.alarm.AlarmService
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

object Utilities {

    private const val TAG = "Utilities"


    fun setUpAlarm(time: Time, alarmId: Int) {

        when (time.meridiem) {
            Meridiem.AM -> {
                val alarmService = AlarmService(App.context)

                val c = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, time.hour.toInt())
                    set(Calendar.MINUTE, time.minutes.toInt())
                    set(Calendar.SECOND, 0)
                }

                alarmService.setExactAlarm(c.timeInMillis, alarmId)
            }
            else -> {
                val alarmService = AlarmService(App.context)

                val c = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, time.hour.toInt() + 12)
                    set(Calendar.MINUTE, time.minutes.toInt())
                    set(Calendar.SECOND, 0)
                }

                alarmService.setExactAlarm(c.timeInMillis, alarmId)
            }
        }

    }


    fun isInternetAvailable(context: Context): Boolean {
        var isConnected = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun inputStreamToJson(inputStream: InputStream): String? {
        var json: String? = null
        try {
            // InputStream is used to read data from a source
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
            return json
        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated: $e")
        }
        return json
    }

    fun stringToTime(stringTime: String): Time {
        val stringArray = stringTime.split(":").toTypedArray()
        val tempHour: String = stringArray[0]
        lateinit var hour: String
        val minute: String = stringArray[1]
        lateinit var meridiem: Meridiem

        if (tempHour.toInt() > 12) {
            meridiem = Meridiem.PM
            hour = (tempHour.toInt() - 12).toString()
        } else {
            meridiem = Meridiem.AM
            hour = tempHour
        }

        return Time(hour, minute, meridiem)
    }
}