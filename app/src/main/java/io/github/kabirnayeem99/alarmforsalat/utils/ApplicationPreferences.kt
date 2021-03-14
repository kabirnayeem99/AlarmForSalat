package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City

class ApplicationPreferences(val context: Context) {

    private var applicationPreferences: ApplicationPreferences? = null
    private var editor: SharedPreferences.Editor
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    init {
        editor = sharedPreferences.edit()
        editor.apply()
    }


    fun getPreferences(context: Context): ApplicationPreferences? {
        if (applicationPreferences == null) {
            applicationPreferences = ApplicationPreferences(context)
        }
        return applicationPreferences
    }

    fun setCity(city: City) {
        editor.putString(Constants.PLACE_NAME_PREFERENCE, city.city)
        editor.putFloat(Constants.PLACE_LAT_PREFERENCE, city.lat.toFloat())
        editor.putFloat(Constants.PLACE_LONG_PREFERENCE, city.lng.toFloat())
        editor.apply()
    }

    fun getLat(): Double {
        return sharedPreferences.getFloat(Constants.PLACE_LAT_PREFERENCE, 0.0f).toDouble()
    }

    fun getLong(): Double {
        return sharedPreferences.getFloat(Constants.PLACE_LONG_PREFERENCE, 0.0f).toDouble()
    }

    fun getCityName(): String {
        val cityName = sharedPreferences.getString(Constants.PLACE_NAME_PREFERENCE, "")
        if (cityName != null) {
            return cityName
        }
        return ""
    }
}