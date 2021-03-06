package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsManager(val context: Context) {

    // creates a data store
    private val dataStore = context.createDataStore(DATA_STORE_NAME)


    companion object {
        val CITY_NAME = preferencesKey<String>("location_city_name")
        val COUNTRY_NAME = preferencesKey<String>("location_country_name")
        val LAC_LONGITUDE = preferencesKey<Float>("location_longitude")
        val LOC_LATITUDE = preferencesKey<Float>("location_latitude")
        const val DATA_STORE_NAME = "settings_pref"
        val instance: SettingsManager by lazy { SettingsManager(App.context) }
        private const val TAG = "SettingsManager"
    }

    /**
     * Sets the user selected city
     * to the data store
     *
     * sets country name
     * sets city name
     * sets latitude, and longitude
     *
     * @param city [City]
     */
    suspend fun setCity(city: City) {
        dataStore.edit { pref ->
            Log.d(TAG, "setCity: current city is $city")
            pref[COUNTRY_NAME] = city.country
            pref[CITY_NAME] = city.city
            pref[LOC_LATITUDE] = city.lat.toFloat()
            pref[LAC_LONGITUDE] = city.lng.toFloat()
        }
    }


    val cityNameFlow: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            return@map preference[CITY_NAME] ?: ""
        }


    val cityLatFlow: Flow<Float> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            return@map preferences[LOC_LATITUDE] ?: 0.0f

        }

    val cityLongFlow: Flow<Float> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            return@map preferences[LAC_LONGITUDE] ?: 0.0f
        }


    val countryNameFlow: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            return@map preference[COUNTRY_NAME] ?: ""
        }


}