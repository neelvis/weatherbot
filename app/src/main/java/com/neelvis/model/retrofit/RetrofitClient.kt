package com.neelvis.model.retrofit

import com.neelvis.model.livedata.LiveDataCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofitClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient.create(ApiInterface::class.java)
    }
}
// what's the purpose?
//val levelType: Level
//if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
//levelType = Level.BODY else levelType = Level.NONE
//
//val logging = HttpLoggingInterceptor()
//logging.setLevel(levelType)
//
//val okhttpClient = OkHttpClient.Builder()
//okhttpClient.addInterceptor(logging)

