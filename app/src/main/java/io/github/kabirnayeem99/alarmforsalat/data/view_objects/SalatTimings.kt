package io.github.kabirnayeem99.alarmforsalat.data.view_objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.kabirnayeem99.alarmforsalat.utils.Constants

@Entity(tableName = Constants.DB_TABLE_NAME_SALAT_TIMINGS)
data class SalatTimings(
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int,
    val Asr: String,
    val Dhuhr: String,
    val Fajr: String,
    val Imsak: String,
    val Isha: String,
    val Maghrib: String,
    val Midnight: String,
    val Sunrise: String,
    val Sunset: String
)