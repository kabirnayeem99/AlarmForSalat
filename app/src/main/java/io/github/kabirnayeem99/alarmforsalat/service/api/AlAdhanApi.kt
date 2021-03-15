package io.github.kabirnayeem99.alarmforsalat.service.api

import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlAdhanApi {
    @GET("v1/timingsByCity")
    suspend fun getAdhanTime(
        @Query("city")
        city: String = "Dhaka",
        @Query("country")
        country: String = "Bangladesh",
        @Query("method")
        method: Int = 4,
        @Query("school")
        school: Int = 4,
    ): Response<AladhanApiResponse>
}