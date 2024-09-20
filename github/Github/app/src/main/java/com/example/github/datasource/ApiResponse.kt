package com.example.github.datasource

sealed class ApiResponse<out T> {

    data class Success<out T>(val data: T) : ApiResponse<T>()

    data class Failure(
        var message: String?,
        var statusCode: Int?,
    ) : ApiResponse<Nothing>()
}