package com.neelvis.model.data

import android.location.Location
import android.location.LocationManager

object LocationService {
    var locationManager: LocationManager? = null

    fun getLocation(): Location? =
        try {
            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            //TODO: Handle exception
            null
        }
}