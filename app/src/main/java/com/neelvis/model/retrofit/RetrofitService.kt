package com.neelvis.model.retrofit

import com.neelvis.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("weather")
    fun getWeatherByGps(@Query("lat") latitude: String, @Query("lon") longitude: String, @Query("appid") apiKey: String): Call<Weather>
}

//    @GET("weather?lat={latitude}&lon={longitude}&appid={apiKey}")
//    fun getWeatherByGps(@Path("latitude") latitude: String, @Path("longitude") longitude: String, @Path("apiKey") apiKey: String): Call<Weather>