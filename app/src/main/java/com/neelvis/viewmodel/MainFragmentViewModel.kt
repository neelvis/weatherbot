package com.neelvis.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neelvis.WeatherBotApplication
import com.neelvis.model.Model
import com.neelvis.model.Weather
import com.neelvis.model.livedata.ApiResponse
import com.neelvis.model.repository.Repository
import com.neelvis.view.MainFragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainFragmentViewModel : ViewModel() {
    private val model = Model()

    var weatherLiveData: LiveData<ApiResponse<Weather>> = askCurrentWeather()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(60_000)
                weatherLiveData = askCurrentWeather()
            }
        }
    }

    private fun askCurrentWeather() = Repository.getWeatherApiCall(model.getLocationData())
}