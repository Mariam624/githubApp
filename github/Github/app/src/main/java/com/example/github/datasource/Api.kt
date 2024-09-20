package com.example.github.datasource

import com.google.gson.GsonBuilder
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Api {

    val client: ApiInterface by lazy {
        buildRetrofitObject().create(ApiInterface::class.java)
    }

    private const val BASE_URL = "https://api.github.com/"
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(::intercept)
            .build()
    }

    private fun intercept(chain: Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        return chain.proceed(requestBuilder.build())
    }

    private fun buildRetrofitObject(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val factory = GsonConverterFactory.create(gson)

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(factory)
            .build()
    }
}