package com.example.sozialchatapp.network


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.178.38:8000/api/") // Meine fastapi
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: OllamaApi by lazy {
        retrofit.create(OllamaApi::class.java)
    }
}