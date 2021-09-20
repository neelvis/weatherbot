package com.neelvis.model.data

import com.neelvis.model.repository.API_KEY

//data class RequestData(val latitude: String = "0.0", val longitude: String = "0.0", val apiKey: String = API_KEY)

class Location() {

    fun getLocationRequestData(): RequestData {

        val location = LocationService.getLocation()

        return RequestData(
            location?.latitude?.toString() ?: "0.0",
            location?.longitude?.toString() ?: "0.0",
            API_KEY
        )
    }
}