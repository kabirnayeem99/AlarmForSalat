package io.github.kabirnayeem99.alarmforsalat.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import kotlinx.coroutines.flow.Flow

@Dao
interface SalatTimingDao {
    @Insert
    suspend fun insert(salatTiming: SalatTiming)

    @Update
    suspend fun update(salatTiming: SalatTiming)

    @Query("SELECT * FROM salat_timings_table")
    fun getSalatTimings(): Flow<List<SalatTiming>>
}