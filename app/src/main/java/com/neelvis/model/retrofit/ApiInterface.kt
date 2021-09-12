package com.neelvis.model.retrofit

import androidx.lifecycle.LiveData
import com.neelvis.model.Weather
import com.neelvis.model.livedata.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather?")
    fun getWeatherByGps(@Query("lat") latitude: String, @Query("lon") longitude: String, @Query("appid") apiKey: String): LiveData<ApiResponse<Weather>>
}