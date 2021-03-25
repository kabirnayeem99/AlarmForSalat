package io.github.kabirnayeem99.alarmforsalat.utils

import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import java.util.*

/**
 * Object class that handles various forms of data
 */
object DataHandler {

    private var salatTimeList = arrayListOf<Time>()

    /**
     * set string salat time list
     */
    fun setTimeInString(salatTimeStringList: ArrayList<String>) {
        for (salatTime in salatTimeStringList) {
            salatTimeList.add(Utilities.stringToTime(salatTime))
        }
    }


    /**
     * Returns a Salat Timing List
     * @return salat timing list [ArrayList]
     */
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


    /**
     * Gets the places from assets
     * And returns in City Object List form
     * @return list of City [List]
     */
    fun getPlacesInCityList(): List<City> {

        val json: String? = Constants.PLACES

        if (json != null) {
            return Gson().fromJson(json, PlacesResponse::class.java)
        }
        return listOf(City("", "", 0.0, 0.0, 20))
    }
}