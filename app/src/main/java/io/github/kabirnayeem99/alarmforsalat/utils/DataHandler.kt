package io.github.kabirnayeem99.alarmforsalat.utils

import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import io.github.kabirnayeem99.alarmforsalat.enum.Meridiem

object DataHandler {

    private var salatTimeList = arrayListOf<Time>()

    fun setTimeInString(salatTimeStringList: ArrayList<String>) {
        for (salatTime in salatTimeStringList) {
            salatTimeList.add(Utilities.stringToTime(salatTime))
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
//        val fajr = SalatTiming(1, "Fajr", fajrTime, true)
//        arrayList.add(fajr)
//        val dhuhr = SalatTiming(2, "Dhuhr", dhuhrTime, false)
//        arrayList.add(dhuhr)
//        val asr = SalatTiming(3, "Asr", asrTime, true)
//        arrayList.add(asr)
//        val maghrib = SalatTiming(4, "Maghrib", maghribTime, false)
//        arrayList.add(maghrib)
//        val isha = SalatTiming(5, "Isha", ishaTime, true)
//        arrayList.add(isha)
        return arrayList
    }
}