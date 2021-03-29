package io.github.kabirnayeem99.alarmforsalat.repos

import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.service.api.RetrofitInstance
import io.github.kabirnayeem99.alarmforsalat.service.db.SalatTimingsDatabase
import kotlinx.coroutines.flow.Flow

class AdhanRepo(private var db: SalatTimingsDatabase) {

    suspend fun getAdhanTime(city: String, country: String) =
        RetrofitInstance.api.getAdhanTime(city, country)


    /**
     * Inserts each wakt to the data base
     */
    suspend fun insert(salatTiming: SalatTiming) = db.getSalatTimingsDao().insert(salatTiming)

    /**
     * Updates each wakt, such as changing the time, or changing on or off
     */
    suspend fun update(salatTiming: SalatTiming) = db.getSalatTimingsDao().update(salatTiming)

    suspend fun delete(salatTiming: SalatTiming) = db.getSalatTimingsDao().delete(salatTiming)

    /**
     * Gets the list of alarms
     */
    fun getSalatTimings(): Flow<List<SalatTiming>> =
        db.getSalatTimingsDao().getSalatTimings()

}