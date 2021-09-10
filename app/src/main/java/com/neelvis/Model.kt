package com.neelvis

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.ContextCompat.getSystemService
import com.neelvis.retrofit.RetrofitClient
import com.neelvis.retrofit.RetrofitService
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.*


data class Weather(val temperature: Int)

class Model(private val context: Context) {

    private lateinit var weatherService: RetrofitService

    private var apiKey = "5a636be8e369e125132a92305ad76460"

    private val locationManager = getSystemService(context, LocationManager::class.java)

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getWeather()
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
            warnAboutGps()
        }
    }

    private fun warnAboutGps() {
        Toast.makeText(context, "Доступ к местоположению отключен. Включите доступ в найстройках.", Toast.LENGTH_LONG).show()
    }

    init {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60_000,
                1000.0f,
                locationListener
            )
        } catch (e: SecurityException) {
            warnAboutGps()
        }
    }

    fun getWeather() = runBlocking {

        val location: Location? = try {
            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: throw Exception("Не могу определить местоположение")
        } catch (e: SecurityException) {
            warnAboutGps()
            null
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        }

        if (location != null) {
            weatherService = RetrofitClient.retrofitService
            launch {
                weatherService.getWeather(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    apiKey
                )
            }

            val apiRequest = "api.openweathermap.org/data/2.5/"
            val retrofit = Retrofit.Builder()
                .baseUrl("$apiRequest")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}