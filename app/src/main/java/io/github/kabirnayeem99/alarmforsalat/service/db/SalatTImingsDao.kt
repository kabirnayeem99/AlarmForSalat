package io.github.kabirnayeem99.alarmforsalat.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming

@Dao
interface SalatTimingDao {
    @Insert
    suspend fun insert(salatTiming: SalatTiming)

    @Update
    suspend fun update(salatTiming: SalatTiming)

    @Query("SELECT * FROM salat_timings_table")
    suspend fun getSalatTimings(): List<SalatTiming>
}