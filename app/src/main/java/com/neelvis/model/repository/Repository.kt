package com.neelvis.model.repository

import androidx.lifecycle.LiveData
import com.neelvis.model.livedata.ApiResponse
import com.neelvis.model.retrofit.RetrofitClient.apiInterface
import com.neelvis.model.data.Weather
import com.neelvis.model.data.RequestData


const val API_KEY = "5a636be8e369e125132a92305ad76460"

interface IRepository{

    fun getWeatherApiCall(requestData: RequestData): LiveData<ApiResponse<Weather>>
}

class Repository: IRepository {

    override fun getWeatherApiCall(requestData: RequestData): LiveData<ApiResponse<Weather>> =
        apiInterface.getWeatherByGps(requestData.latitude, requestData.longitude, requestData.apiKey)
}