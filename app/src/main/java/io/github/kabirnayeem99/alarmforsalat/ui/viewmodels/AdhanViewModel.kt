package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class AdhanViewModel(
    private val repo: AdhanRepo,
    city: String,
    country: String,
) : ViewModel() {

    companion object {
        private const val TAG = "AdhanViewModel"
    }

    private val settingsManager = SettingsManager.instance

    val adhanTime: MutableLiveData<Resource<AladhanApiResponse>> = MutableLiveData()

    init {
        getAdhanTime(city, country)
    }


    private fun getAdhanTime(city: String, country: String) = viewModelScope.launch {
        adhanTime.postValue(Resource.Loading())

        val cityName: String = settingsManager.cityName.first()
        val countryName: String = settingsManager.countryName.first()
        val response: Response<AladhanApiResponse> = repo.getAdhanTime(cityName, countryName)



        adhanTime.postValue(handleAdhanTimeResponse(response))
    }


    private fun handleAdhanTimeResponse(response: Response<AladhanApiResponse>): Resource<AladhanApiResponse> {

        if (response.isSuccessful) {
            response.body()?.let {

                Log.d(
                    TAG,
                    "handleAdhanTimeResponse: the response was successful ${response.body()}"
                )
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())


//        return Resource.Loading()

    }
}