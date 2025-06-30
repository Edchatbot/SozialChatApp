package com.example.sozialchatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sozialchatapp.models.ChatHistoryResponse
import com.example.sozialchatapp.network.OllamaApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ChatViewModel : ViewModel() {

    // Interner Zustand
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    // Retrofit-Instanz nur einmal erzeugen
    private val api: OllamaApi = Retrofit.Builder()
        .baseUrl("http://192.168.178.XX:8000/") // <== IP-Adresse anpassen
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(OllamaApi::class.java)

    // Daten vom Server laden
    fun loadChatHistory(user: String = "guest") {
        viewModelScope.launch {
            try {
                val response: Response<ChatHistoryResponse> = api.getChatHistory(user)

                if (response.isSuccessful) {
                    val chatList = response.body()?.chats ?: emptyList()
                    _chatHistory.value = chatList
                } else {
                    println("Serverfehler: Code ${response.code()}")
                }
            } catch (e: Exception) {
                println("Verbindungsfehler: ${e.message}")
            }
        }
    }
}

