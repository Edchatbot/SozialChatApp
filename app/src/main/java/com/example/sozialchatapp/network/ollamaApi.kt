package com.example.sozialchatapp.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.Response
import com.example.sozialchatapp.models.ChatHistoryResponse


interface OllamaApi {
    @Headers("Content-Type: application/json")
    @POST("api/chat")
    suspend fun generateText(@Body request: GenerateRequest): GenerateResponse
    @GET("api/chat/history")
    suspend fun getChatHistory(@Query("user") user: String): Response<ChatHistoryResponse>
}