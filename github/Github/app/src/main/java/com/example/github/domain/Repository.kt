package com.example.github.domain

import com.example.github.datasource.Api
import com.example.github.datasource.ApiResponse
import com.example.github.datasource.makeCall
import com.example.github.presentation.data.User

class Repository {

    suspend fun getUsers(): ApiResponse<List<User>> = makeCall {
        Api.client.getUsers()
    }

    suspend fun getUser(userName: String) = makeCall {
        Api.client.getUser(userName)
    }
}