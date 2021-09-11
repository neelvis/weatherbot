package com.neelvis.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neelvis.WeatherBotApplication
import com.neelvis.model.Model
import com.neelvis.model.Weather
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    // TODO: Implement the
    private val model = Model()
    private var _weatherData = MutableLiveData<Weather>()
    val weatherData:LiveData<Weather>
        get() = _weatherData

    init {
        GlobalScope.launch(Dispatchers.IO) {
            askCurrentWeather()
        }
    }

    private suspend fun askCurrentWeather() {
        while (true) {
            val weather = model.getWeather()
            if (weather != null)
                _weatherData.postValue(weather!!)
            delay(5000)
        }
    }
}