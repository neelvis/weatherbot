package com.neelvis.model.data

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import com.google.android.gms.location.LocationServices
import com.neelvis.model.repository.API_KEY


data class RequestData(val latitude: String = "0.0", val longitude: String = "0.0", val apiKey: String = API_KEY)

object  LocationService : Service() {

    var locationManager: LocationManager? = null
    var appContext: Context? = null
    var currentLocation: Location? = null

    fun getLocation(): Location? {
        val client = LocationServices.getFusedLocationProviderClient(appContext)
        var location: Location? = null

        return try {
//            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            client.lastLocation.addOnSuccessListener {
                location = it
            }
            location
        } catch (e: SecurityException) {
            //TODO: Handle exception
            null
        }
    }

    fun getLocationRequestData(): RequestData {

        val location = LocationService.getLocation()

        return RequestData(
            location?.latitude?.toString() ?: "0.0",
            location?.longitude?.toString() ?: "0.0",
            API_KEY
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}