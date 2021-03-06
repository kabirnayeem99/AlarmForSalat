package io.github.kabirnayeem99.alarmforsalat.utils

import java.io.InputStream

object Constants {
    const val EXTRA_SALAT_NAME: String = "extra_salat_name"
    const val EXTRA_EXACT_ALARM: String = "extra_exact_alarm"
    const val SET_EXACT_ALARM: String = "set_exact_alarm"
    const val ACTION_SET_REPETITIVE_EXACT: String = "set_repetitive_alarm"
    const val PLACE_COUNTRY_NAME_PREFERENCE: String = "place_country"
    const val SHARED_PREFERENCES_NAME: String =
        "com.github.kabirnayeem99.alarmforsalat.application_preferences"
    const val PLACE_LONG_PREFERENCE: String = "place_long"
    const val PLACE_NAME_PREFERENCE: String = "place_name"
    const val PLACE_LAT_PREFERENCE: String = "place_lat"
    private const val PLACES_FILE_NAME: String = "places.json"
    const val BASE_URL: String = "https://api.aladhan.com"
    const val DB_NAME = "alarm_for_salat_db"
    const val DB_TABLE_NAME_SALAT_TIMINGS = "salat_timings_table"
    private val inputStream: InputStream = App.context.assets.open(PLACES_FILE_NAME)
    val PLACES = Utilities.inputStreamToJson(inputStream)

    val SALATS = hashMapOf(1 to "Fajr", 2 to "Dhuhr", 3 to "Asr", 4 to "Maghrib", 5 to "Isha")


}