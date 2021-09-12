package com.neelvis.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neelvis.model.Weather
import com.neelvis.model.retrofit.RetrofitClient.apiInterface
import com.neelvis.model.RequestData
import com.neelvis.model.livedata.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object Repository {

    const val API_KEY = "5a636be8e369e125132a92305ad76460"
//    val weatherLiveData = LiveData<Weather>()

    fun getWeatherApiCall(requestData: RequestData): LiveData<ApiResponse<Weather>> =
        apiInterface.getWeatherByGps(requestData.latitude, requestData.longitude, requestData.apiKey)


//        weatherApiCall.enqueue(object : Callback<Weather> {
//            override fun onFailure(call: Call<Weather>, t: Throwable) {
//                Log.d("APICALL", "Weather request failure")
//            }
//
//            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
//                if (response.isSuccessful) {
//                    weatherLiveData.value = response.body()!!
//                }
//            }
//        })

//        return weatherLiveData

}