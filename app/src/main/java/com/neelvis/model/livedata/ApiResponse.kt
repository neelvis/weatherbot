package com.neelvis.model.livedata

import retrofit2.Response

enum class HttpResponseCodes(val code: Int) {
    NO_CONNECT(204)
}

internal const val UNKNOWN_CODE = -1

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == HttpResponseCodes.NO_CONNECT.code) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                ApiErrorResponse(
                    response.code(),
                    response.errorBody()?.string() ?: response.message()
                )
            }
        }

        fun <T> create(errorCode: Int, error: Throwable): ApiErrorResponse<T> =
            ApiErrorResponse(
                errorCode,
                error.message ?: "Unknown API Response error"
            )
    }
}

class ApiEmptyResponse<T>: ApiResponse<T>()
data class ApiSuccessResponse<T> (val body: T): ApiResponse<T> ()
data class ApiErrorResponse<T> (val errorCode: Int, val errorMessage: String): ApiResponse<T> ()