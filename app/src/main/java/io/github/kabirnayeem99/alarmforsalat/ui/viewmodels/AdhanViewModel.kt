package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.AladhanApiResponse
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class AdhanViewModel(private val repo: AdhanRepo) : ViewModel() {

    private val settingsManager = SettingsManager.instance

    companion object {
        private const val TAG = "AdhanViewModel"
    }

    // Is success, error or loading based on the state
    val adhanTime: MutableLiveData<Resource<AladhanApiResponse>> = MutableLiveData()


    init {
        getAdhanTime()
    }


    fun insert(salatTiming: SalatTiming) = viewModelScope.launch { repo.insert(salatTiming) }
    fun update(salatTiming: SalatTiming) = viewModelScope.launch { repo.insert(salatTiming) }
    fun getSalatTimings(): Flow<List<SalatTiming>> = repo.getSalatTimings()

    private fun getAdhanTime() = viewModelScope.launch {
        adhanTime.postValue(Resource.Loading())


        val cityName: String = settingsManager.cityNameFlow.first()
        val countryName: String = settingsManager.countryNameFlow.first()

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

    }
}