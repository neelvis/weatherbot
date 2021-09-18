package com.neelvis.model.livedata

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val observableType =
            CallAdapter.Factory.getParameterUpperBound(
                0,
                returnType as ParameterizedType
            ) as? ParameterizedType
                ?: throw(IllegalArgumentException("Resource must be parametrized"))

        return LiveDataCallAdapter<Any> (CallAdapter.Factory.getParameterUpperBound(0, observableType))
    }
}