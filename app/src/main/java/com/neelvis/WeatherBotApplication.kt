package com.neelvis

import android.app.Application
import android.content.Context
import com.neelvis.model.LocationService

class WeatherBotApplication: Application() {

    override fun onCreate() {
        super.onCreate()
//        LocationService.appContext = applicationContext
    }
}