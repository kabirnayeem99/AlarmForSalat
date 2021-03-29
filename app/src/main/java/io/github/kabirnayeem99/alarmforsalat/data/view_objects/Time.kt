package io.github.kabirnayeem99.alarmforsalat.data.view_objects

import androidx.room.Entity
import io.github.kabirnayeem99.alarmforsalat.enum.Meridiem


@Entity
data class Time(
    var hour: String = "",
    var minutes: String = "",
    var meridiem: Meridiem = Meridiem.AM,
)