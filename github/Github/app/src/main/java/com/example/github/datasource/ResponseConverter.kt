package com.example.github.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

suspend inline fun <T> makeCall(crossinline apiCall: suspend () -> Response<T>) =
    withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            convertResponse(response)
        } catch (e: UnknownHostException) {
            ApiResponse.Failure("Something wrong happened", null)
        }
    }

fun <T> convertResponse(response: Response<T>): ApiResponse<T> {
    val statusCode = response.code()

    if (response.isSuccessful) {
        val body = response.body() ?: return ApiResponse.Failure("Null body.", statusCode)
        return ApiResponse.Success(body)
    }

    return ApiResponse.Failure(response.message() ?: "Converting failed", statusCode)
}