package com.example.github.datasource

import com.example.github.presentation.data.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/users?per_page=30")
    suspend fun getUsers(): Response<List<User>>

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>
}