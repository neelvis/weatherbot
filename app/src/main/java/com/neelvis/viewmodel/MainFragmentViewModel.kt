package com.neelvis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neelvis.model.data.Location
import com.neelvis.model.data.Weather
import com.neelvis.model.livedata.ApiResponse
import com.neelvis.model.repository.Repository
import kotlinx.coroutines.*

class MainFragmentViewModel : ViewModel() {
    private val model = Location()
    private val repository = Repository()

    var weatherLiveData: LiveData<ApiResponse<Weather>> = askCurrentWeather()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(60_000)
                weatherLiveData = askCurrentWeather()
            }
        }
    }

    private fun askCurrentWeather() = repository.getWeatherApiCall(model.getLocationData())
}