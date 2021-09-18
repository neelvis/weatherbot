package com.neelvis

import android.app.Application

class WeatherBotApplication: Application() {

    override fun onCreate() {
        super.onCreate()
//        LocationService.appContext = applicationContext
    }
}