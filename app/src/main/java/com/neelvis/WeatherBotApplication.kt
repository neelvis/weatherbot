package com.neelvis

import android.app.Application
import com.neelvis.model.data.LocationService

class WeatherBotApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LocationService.appContext = applicationContext
    }
}