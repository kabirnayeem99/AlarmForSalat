package io.github.kabirnayeem99.alarmforsalat.utils

object Constants {
    const val EXTRA_EXACT_ALARM: String = "extra_exact_alarm"
    const val SET_EXACT_ALARM: String = "set_exact_alarm"
    const val PLACE_COUNTRY_NAME_PREFERENCE: String = "place_country"
    const val SHARED_PREFERENCES_NAME: String =
        "com.github.kabirnayeem99.alarmforsalat.application_preferences"
    const val PLACE_LONG_PREFERENCE: String = "place_long"
    const val PLACE_NAME_PREFERENCE: String = "place_name"
    const val PLACE_LAT_PREFERENCE: String = "place_lat"
    const val placesFileName: String = "places.json"
    const val BASE_URL: String = "https://api.aladhan.com"
    var SALAT_NAME_LIST = arrayListOf<String>("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
}