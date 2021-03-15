package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AdhanViewModel(
    private val repo: AdhanRepo,
    city: String,
    country: String,
) : ViewModel() {
    val adhanTime: MutableLiveData<Resource<AladhanApiResponse>> = MutableLiveData()

    init {
        getAdhanTime(city, country)
    }

    private fun getAdhanTime(city: String, country: String) = viewModelScope.launch {
        adhanTime.postValue(Resource.Loading())

        val response = repo.getAdhanTime(city, country)

        adhanTime.postValue(handleAdhanTimeResponse(response))
    }

    private fun handleAdhanTimeResponse(response: Response<AladhanApiResponse>): Resource<AladhanApiResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { aladhanApiResponse ->
                return Resource.Success(aladhanApiResponse)
            }
        }

        return Resource.Error(response.message())
    }
}