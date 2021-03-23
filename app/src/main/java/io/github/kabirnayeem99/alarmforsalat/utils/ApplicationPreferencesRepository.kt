package io.github.kabirnayeem99.alarmforsalat.utils

import android.content.Context
import android.util.Log
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


//class UserPreferencesRepository(context: Context) {
//
//    private val sharedPreferences =
//        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
//
//    private val _userPreferencesFlow = MutableStateFlow(userPreferences)
//    val userPreferencesFlow: StateFlow<UserPreferences> = _userPreferencesFlow
//
//    private val userPreferences: UserPreferences
//        get() {
//            val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
//            val filter = sharedPreferences.getString(FILTER_KEY, Filter.NONE.name)
//            return UserPreferences(
//                sortOrder = SortOrder.valueOf(order ?: SortOrder.NONE.name),
//                filter = Filter.valueOf(filter ?: Filter.NONE.name)
//            )
//        }
//
//    fun updateFilter(filter: Filter) {
//        _userPreferencesFlow.value = userPreferences.copy(filter = filter)
//        sharedPreferences.edit {
//            putString(FILTER_KEY, filter.name)
//        }
//    }
//
//    fun updateSortOrder(sortOrder: SortOrder) {
//        _userPreferencesFlow.value = userPreferences.copy(sortOrder = sortOrder)
//        sharedPreferences.edit {
//            putString(SORT_ORDER_KEY, sortOrder.name)
//        }
//    }
//
//    companion object {
//        private const val USER_PREFERENCES_NAME = "user_preferences"
//        private const val SORT_ORDER_KEY = "sort_order"
//        private const val FILTER_KEY = "filter"
//    }
//}
//
//enum class SortOrder {
//    NONE,
//    BY_NAME,
//    BY_YEAR,
//    BY_RATING
//}
//
//enum class Filter {
//    NONE,
//    HORROR,
//    ACTION,
//    DRAMA
//}
//

class ApplicationPreferencesRepository(val context: Context) {

    companion object {
        private const val TAG = "ApplicationPreferencesR"
    }

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(
            Constants.SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

    private val _applicationPreferencesFlow = MutableStateFlow(applicationPreferences)
    val applicationPreferencesFlow: StateFlow<ApplicationPreferences> =
        _applicationPreferencesFlow


    private val applicationPreferences: ApplicationPreferences
        get() {
            val latitude = sharedPreferences.getFloat(Constants.PLACE_LAT_PREFERENCE, 0f)
            val longitude = sharedPreferences.getFloat(Constants.PLACE_LONG_PREFERENCE, 0f)
            val cityName = sharedPreferences.getString(Constants.PLACE_NAME_PREFERENCE, "")!!
            val countryName =
                sharedPreferences.getString(Constants.PLACE_COUNTRY_NAME_PREFERENCE, "")!!

            return ApplicationPreferences(
                latitude, longitude, cityName, countryName
            )
        }


    fun updateCity(city: City) {

        Log.d(TAG, "updateCity: setting city name to ${city.city}")
        _applicationPreferencesFlow.value = applicationPreferences.copy(
            cityName = city.city, latitude = city.lat.toFloat(),
            longitude = city.lng.toFloat(), countryName = city.country
        )
    }

    fun getCityName(): String {
        Log.d(TAG, "getCityName: getting city name ${applicationPreferences.cityName} ")
        return applicationPreferences.cityName
    }

    fun getCountryName(): String {
        return applicationPreferences.countryName
    }

    fun getLatitude(): Double {
        return applicationPreferences.latitude.toDouble()
    }


    fun getLongtitude(): Double {
        return applicationPreferences.longitude.toDouble()
    }
//
//
//    private var applicationPreferencesRepository: ApplicationPreferencesRepository? = null
//    private var editor: SharedPreferences.Editor
////    private var sharedPreferences: SharedPreferences =
////        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//
//    val dataStore: DataStore<Preferences> = context.applicationContext.createDataStore(
//        name = Constants.SHARED_PREFERENCES_NAME,
//        migrations = listOf(SharedPreferencesMigration(context, Constants.SHARED_PREFERENCES_NAME))
//    )
//
//    init {
//        editor = sharedPreferences.edit()
//        editor.apply()
//    }
//
//
//    fun getPreferences(context: Context): ApplicationPreferencesRepository? {
//        if (applicationPreferencesRepository == null) {
//            applicationPreferencesRepository = ApplicationPreferencesRepository(context)
//        }
//        return applicationPreferencesRepository
//    }
//
//    fun setLocation(city: City) {
//        editor.putString(Constants.PLACE_NAME_PREFERENCE, city.city)
//        editor.putString(Constants.PLACE_COUNTRY_NAME_PREFERENCE, city.country)
//        editor.putFloat(Constants.PLACE_LAT_PREFERENCE, city.lat.toFloat())
//        editor.putFloat(Constants.PLACE_LONG_PREFERENCE, city.lng.toFloat())
//        editor.apply()
//    }
//
//    val userPreferencesRepositoryFlow: Flow<ApplicationPreferencesRepository> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preferences ->
//            UserPreferences(
//                sortOrder = SortOrder.valueOf(preferences[SORT_ORDER] ?: SortOrder.NONE.name),
//                filter = Filter.valueOf(preferences[FILTER] ?: Filter.NONE.name)
//            )
//        }
//
//
//    suspend fun setLocationDs(city: City) {
//        dataStore.edit { preferences ->
//            preferences[Preferences.Key<String>()] = city.city
//        }
//    }
//
//    fun getLat(): Double {
//        return sharedPreferences.getFloat(Constants.PLACE_LAT_PREFERENCE, 0.0f).toDouble()
//    }
//
//    fun getLong(): Double {
//        return sharedPreferences.getFloat(Constants.PLACE_LONG_PREFERENCE, 0.0f).toDouble()
//    }
//
//    fun getCityName(): String {
//        val cityName = sharedPreferences.getString(Constants.PLACE_NAME_PREFERENCE, "")
//        if (cityName != null) {
//            return cityName
//        }
//        return ""
//    }
//
//    fun getCountryName(): String {
//        val countryName = sharedPreferences.getString(Constants.PLACE_COUNTRY_NAME_PREFERENCE, "")
//        if (countryName != null) {
//            return countryName
//        }
//        return ""
//    }
}

data class ApplicationPreferences(
    val latitude: Float,
    val longitude: Float,
    val cityName: String,
    val countryName: String
)
