package com.neelvis

import android.app.Application
import android.content.Context

class WeatherBotApplication: Application() {
    companion object {
        private lateinit var context: Context

        fun getAppContext(): Context {
            return WeatherBotApplication.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        WeatherBotApplication.context = applicationContext
    }

}