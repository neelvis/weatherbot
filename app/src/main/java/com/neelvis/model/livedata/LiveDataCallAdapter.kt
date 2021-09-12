package com.neelvis.model.livedata

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LiveDataCallAdapter <R>(private val repsonseType: Type): CallAdapter<R, LiveData<ApiResponse<R>>> {
    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> = object : LiveData<ApiResponse<R>>() {
        private var isSucceeded = false

        override fun onActive() {
            super.onActive()
            if (!isSucceeded)
                enqueue()
        }

        override fun onInactive() {
            super.onInactive()
            dequeue()
        }

        private fun enqueue() {
            call.enqueue(object : Callback<R> {
                override fun onFailure(call: Call<R>, t: Throwable) {
                    postValue(ApiResponse.create(UNKNOWN_CODE, t))
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    postValue(ApiResponse.create(response))
                    isSucceeded = true
                }
            })
        }

        private fun dequeue() {
            if(call.isExecuted)
                call.cancel()
        }
    }

    override fun responseType(): Type = responseType()
}