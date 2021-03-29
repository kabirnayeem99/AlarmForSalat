package io.github.kabirnayeem99.alarmforsalat.utils

import android.util.Log
import com.azan.Azan
import com.azan.Method
import com.azan.Time
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import us.dustinj.timezonemap.TimeZoneMap
import java.util.*
import java.util.concurrent.TimeUnit

class AdhanTimeUtilities(var latitude: Double, var longitude: Double) {

    companion object {
        private const val TAG = "AdhanTimeUtilities"
    }


    suspend fun getSalatTimingList(): List<SalatTiming> {

        val today = SimpleDate(GregorianCalendar())
        val location = Location(latitude, longitude, getGmtDiff(latitude, longitude), 0)
        val azan = Azan(location, Method.MUSLIM_LEAGUE)
        val prayerTimes = azan.getPrayerTimes(today)


        val fajr: Time = prayerTimes.fajr()
        val dhuhr: Time = prayerTimes.thuhr()
        val asr: Time = prayerTimes.assr()
        val maghrib: Time = prayerTimes.maghrib()
        val isha: Time = prayerTimes.ishaa()

        val adhanList = listOf(
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
                false
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
                    "Fajr --> $fajr\n" +
                    "Dhuhr --> $dhuhr\n" +
                    "Asr --> $asr\n" +
                    "Maghrib --> $maghrib\n" +
                    "Isha --> $isha\n" +
                    "-----------------------------------------------"
        )
    }

    private fun getGmtDiff(latitude: Double, longitude: Double): Double {
        val map =
            TimeZoneMap.forRegion(
                latitude - 0.01,
                longitude - 0.01,
                latitude + 0.01,
                longitude + 0.01
            )
        val location = map.getOverlappingTimeZone(latitude, longitude)?.zoneId
        val timeZone = TimeZone.getTimeZone(location)
        Log.d(TAG, "getGmtDiff: ${timeZone.displayName}")
        val gmtOffset = timeZone.rawOffset
        return TimeUnit.HOURS.convert(gmtOffset.toLong(), TimeUnit.MILLISECONDS).toDouble()
    }

}