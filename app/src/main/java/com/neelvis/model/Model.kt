package com.neelvis.model

import com.neelvis.model.repository.Repository.API_KEY
import com.neelvis.view.MainActivity

data class RequestData(val latitude: String = "0.0", val longitude: String = "0.0", val apiKey: String = API_KEY)
data class LocationCoords(val latitude: String, val longitude: String)

class Model() {

    fun getLocationData(): RequestData {

        val location = LocationService.getLocation()

        return RequestData(
            location?.latitude?.toString() ?: "0.0",
            location?.longitude?.toString() ?: "0.0",
            API_KEY
        )
    }
}