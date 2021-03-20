package io.github.kabirnayeem99.alarmforsalat.data.view_objects

import androidx.room.Entity
import io.github.kabirnayeem99.alarmforsalat.utils.Constants


data class AladhanApiResponse(
    val code: Int = 0,
    val `data`: ResponseData,
    val status: String
)