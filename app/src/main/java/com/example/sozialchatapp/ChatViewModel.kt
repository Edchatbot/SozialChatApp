package com.example.sozialchatapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.sozialchatapp.models.ChatMessage
import com.example.sozialchatapp.network.GenerateRequest
import com.example.sozialchatapp.network.RetrofitClient.api
import kotlinx.coroutines.launch


class ChatViewModel : ViewModel() {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    fun loadChatHistory(user: String) {
        viewModelScope.launch {
            try {
                val response = api.getChatHistory(user)
                _chatHistory.value = response.chatList  // Wichtig!
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Fehler beim Laden des Verlaufs", e)
            }
        }
    }

    fun sendMessage(user: String, prompt: String) {
        viewModelScope.launch {
            try {
                val result = api.generateText(GenerateRequest(prompt = prompt, user = user))
                val newMessage = ChatMessage(text = String, isUser = Boolean)
                _chatHistory.value += newMessage  // Chatverlauf lokal erweitern
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Fehler beim Senden", e)
            }
        }
    }
}