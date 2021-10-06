package com.neelvis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neelvis.WeatherBotApplication
import com.neelvis.model.data.LocationUpdateFgService
import com.neelvis.model.data.Weather
import com.neelvis.model.livedata.ApiResponse
import com.neelvis.model.repository.Repository
import kotlinx.coroutines.*

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
//    private val location = Location()
    private val repository = Repository()

    var weatherLiveData: LiveData<ApiResponse<Weather>> = askCurrentWeather()
    val locationUpdateService = LocationUpdateFgService()

    init {
        locationUpdateService.requestLocationUpdates()

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(60_000)
                weatherLiveData = askCurrentWeather()
            }
        }
    }

    private fun askCurrentWeather() = repository.getWeatherApiCall(LocationUpdateFgService.getLocationRequestData())


}