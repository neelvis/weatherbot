package com.neelvis.model

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.neelvis.WeatherBotApplication
import com.neelvis.model.retrofit.RetrofitClient
import com.neelvis.model.retrofit.RetrofitService
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Model(private val context: Context = WeatherBotApplication.getAppContext()) {

    private lateinit var weatherService: RetrofitService

    private var apiKey = "5a636be8e369e125132a92305ad76460"

    private val locationManager = getSystemService(context, LocationManager::class.java)

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

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
            //TODO implement "go to settings" menu to turn on GPS
            //TODO use mobile or wifi to get GPS location
        }
    }

    suspend fun getWeather(): Weather? {

        val location: Location? = try {
            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: throw Exception()
        } catch (e: SecurityException) {
            warnAboutGps()
            null
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        } ?: return null

        weatherService = RetrofitClient.retrofitService


        val weatherCallback = weatherService.getWeatherByGps(
            location!!.latitude.toString(),
            location!!.longitude.toString(),
            apiKey
        )

        var weather: Weather? = null
        weatherCallback.enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                weather = response.body()
                if (!response.isSuccessful)
                    Toast.makeText(context, "response error", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

//        suspend fun <T> Call<T>.await(): T? = suspendCoroutine { continuation ->
//            this.enqueue(object : Callback<T> {
//                override fun onResponse(call: Call<T>, response: Response<T>) {
//                    continuation.resume(response.body())
//                }
//                override fun onFailure(call: Call<T>, t: Throwable) {
//                    continuation.resumeWithException(t)
//                }
//            })
//        }
        return weather
    }
}