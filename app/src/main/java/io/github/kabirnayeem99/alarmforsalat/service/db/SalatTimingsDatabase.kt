package io.github.kabirnayeem99.alarmforsalat.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.utils.Constants


@Database(entities = [SalatTiming::class], exportSchema = false, version = 1)
abstract class SalatTimingsDatabase : RoomDatabase() {
    abstract fun getSalatTimingsDao(): SalatTimingDao

    companion object {
        @Volatile
        private var instance: SalatTimingsDatabase? = null

        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SalatTimingsDatabase::class.java,
                Constants.DB_NAME
            ).build()
    }

}
