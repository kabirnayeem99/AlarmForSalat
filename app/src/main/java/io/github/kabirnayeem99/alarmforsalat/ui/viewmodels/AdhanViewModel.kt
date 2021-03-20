package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
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

    val adhanTime: MutableLiveData<Resource<AladhanApiResponse>> = MutableLiveData()

    init {
        getAdhanTime(city, country)
    }


    private fun getAdhanTime(city: String, country: String) = viewModelScope.launch {
        adhanTime.postValue(Resource.Loading())

        val response: Response<AladhanApiResponse> = repo.getAdhanTime(city, country)



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