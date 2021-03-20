package io.github.kabirnayeem99.alarmforsalat.repos

import io.github.kabirnayeem99.alarmforsalat.service.api.RetrofitInstance
import io.github.kabirnayeem99.alarmforsalat.service.db.SalatTimingsDatabase
import retrofit2.Retrofit

class AdhanRepo(private var db: SalatTimingsDatabase) {

    suspend fun getAdhanTime(city: String, country: String) =
        RetrofitInstance.api.getAdhanTime(city, country)


}