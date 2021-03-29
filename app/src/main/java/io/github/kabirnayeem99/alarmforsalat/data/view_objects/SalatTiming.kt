package io.github.kabirnayeem99.alarmforsalat.data.view_objects

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.kabirnayeem99.alarmforsalat.utils.Constants


@Entity(tableName = Constants.DB_TABLE_NAME_SALAT_TIMINGS)
data class SalatTiming(
    @PrimaryKey
    val id: Int? = null,
    val name: String,
    @Embedded
    val time: Time,
    val toggle: Boolean
)