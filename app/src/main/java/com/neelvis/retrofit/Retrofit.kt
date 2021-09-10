package com.neelvis.retrofit

import com.neelvis.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getRetrofitClient(baseUrl: String): Retrofit = retrofit ?: Retrofit.Builder()
                                                    .baseUrl(baseUrl)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()


    private val baseUrl = "api.openweathermap.org/data/2.5/"

    val retrofitService: RetrofitService
        get() = getRetrofitClient(baseUrl).create(RetrofitService::class.java)
}


interface RetrofitService {
    @GET("weather?lat={latitude}&lon={.longitude}&appid={apiKey}")
    suspend fun getWeather(@Path("latitude") latitude: String, @Path("longitude") longitude: String, @Path("apiKey") apiKey: String): Call<Weather>
}

