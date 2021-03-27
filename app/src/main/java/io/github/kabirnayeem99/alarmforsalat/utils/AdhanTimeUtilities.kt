package io.github.kabirnayeem99.alarmforsalat.utils

import android.util.Log
import com.azan.Azan
import com.azan.Method
import com.azan.Time
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import java.util.*

class AdhanTimeUtilities(var latitude: Double, var longitude: Double) {

    companion object {
        private const val TAG = "AdhanTimeUtilities"
    }


    suspend fun getSalatTimingList(): List<SalatTiming> {

        val today = SimpleDate(GregorianCalendar())
        val location = Location(latitude, longitude, Utilities.getGmtDiff(), 0)
//        val location = Location(30.045411, 31.236735, 2.0, 0)
        val azan = Azan(location, Method.MUSLIM_LEAGUE)
        val prayerTimes = azan.getPrayerTimes(today)


        val fajr: Time = prayerTimes.fajr()
        val dhuhr: Time = prayerTimes.thuhr()
        val asr: Time = prayerTimes.assr()
        val maghrib: Time = prayerTimes.maghrib()
        val isha: Time = prayerTimes.ishaa()

        val adhanList = listOf<SalatTiming>(
            SalatTiming(
                1,
                "Fajr",
                Utilities.azanTimeToMyTime(prayerTimes.fajr()),
                true
            ),
            SalatTiming(
                2,
                "Dhuhr",
                Utilities.azanTimeToMyTime(prayerTimes.thuhr()),
                true
            ),
            SalatTiming(
                3,
                "Asr",
                Utilities.azanTimeToMyTime(prayerTimes.assr()),
                true
            ),
            SalatTiming(
                4,
                "Maghrib",
                Utilities.azanTimeToMyTime(prayerTimes.maghrib()),
                true
            ),
            SalatTiming(
                5,
                "Isha",
                Utilities.azanTimeToMyTime(prayerTimes.ishaa()),
                true
            ),
        )

        logAdhan(fajr, dhuhr, asr, maghrib, isha)

        return adhanList
    }

    private fun logAdhan(fajr: Time, dhuhr: Time, asr: Time, maghrib: Time, isha: Time) {


        Log.d(
            TAG, "initialisation of class: \n" +
                    "----------------results------------------------\n" +
                    "Timezone --> ${TimeZone.getTimeZone("Bangladesh/Dhaka")}\n" +
                    "Fajr --> $fajr\n" +
                    "Dhuhr --> $dhuhr\n" +
                    "Asr --> $asr\n" +
                    "Maghrib --> $maghrib\n" +
                    "Isha --> $isha\n" +
                    "-----------------------------------------------"
        )
    }

}