package io.github.kabirnayeem99.alarmforsalat.repos

import io.github.kabirnayeem99.alarmforsalat.service.api.RetrofitInstance

class AdhanRepo() {

    suspend fun getAdhanTime(city: String, country: String) =
        RetrofitInstance.api.getAdhanTime(city, country)
}