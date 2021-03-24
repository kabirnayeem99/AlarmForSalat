package io.github.kabirnayeem99.alarmforsalat.utils

import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import io.github.kabirnayeem99.alarmforsalat.service.alarm.AlarmService
import java.util.*

object DataHandler {
    private const val TAG = "DataHandler"

    private var salatTimeList = arrayListOf<Time>()

    fun setTimeInString(salatTimeStringList: ArrayList<String>) {
        for ((index, salatTime) in salatTimeStringList.withIndex()) {
            salatTimeList.add(Utilities.stringToTime(salatTime).also {
                setUpAlarm(it.hour.toInt(), it.minutes.toInt(), index)
            })
        }
    }

    private fun setUpAlarm(hour: Int, minute: Int, alarmId: Int) {

        when (alarmId) {
            0 -> {
                val alarmService = AlarmService(App.context)

                val c = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }

                alarmService.setExactAlarm(c.timeInMillis, alarmId)
            }
            else -> {
                val alarmService = AlarmService(App.context)

                val c = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour + 12)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }

                alarmService.setExactAlarm(c.timeInMillis, alarmId)
            }
        }

    }

    fun initialiseData(
    ): ArrayList<SalatTiming> {
        val arrayList = arrayListOf<SalatTiming>()

        for (index in 0..4) {
            arrayList.add(
                SalatTiming(
                    index,
                    Constants.SALAT_NAME_LIST[index],
                    salatTimeList[index],
                    true
                )
            )
        }
        return arrayList
    }


    fun getPlacesInCityList(): List<City> {

        val json: String? = Constants.PLACES

        if (json != null) {
            return Gson().fromJson(json, PlacesResponse::class.java)
        }
        return listOf(City("", "", 0.0, 0.0, 20))
    }
}