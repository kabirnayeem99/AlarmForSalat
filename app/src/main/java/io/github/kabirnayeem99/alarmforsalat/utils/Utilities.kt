package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import io.github.kabirnayeem99.alarmforsalat.enum.Meridiem
import java.io.InputStream
import java.nio.charset.Charset

object Utilities {

    private const val TAG = "Utilities"



    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
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