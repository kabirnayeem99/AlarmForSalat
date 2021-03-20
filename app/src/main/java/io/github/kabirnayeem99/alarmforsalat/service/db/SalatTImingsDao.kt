package io.github.kabirnayeem99.alarmforsalat.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTimings

@Dao
interface SalatTimingsDao {
    @Insert
    suspend fun insert(salatTimings: SalatTimings)

    @Update
    suspend fun update(salatTimings: SalatTimings)

    @Query("SELECT * FROM salat_timings_table")
    suspend fun getSalatTimings(): List<SalatTimings>
}