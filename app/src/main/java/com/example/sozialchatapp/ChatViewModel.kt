package com.example.sozialchatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.sozialchatapp.models.ChatMessage
import com.example.sozialchatapp.network.RetrofitClient.api
import kotlinx.coroutines.launch


class ChatViewModel : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    fun addUserMessage(text: String) {
        val newMessage = ChatMessage(text = text, isUser = true)
        _chatMessages.value += newMessage
        // Optional: persistieren
    }

    fun addBotMessage(text: String) {
        val newMessage = ChatMessage(text = text, isUser = false)
        _chatMessages.value += newMessage
        // Optional: persistieren
    }

    fun loadChatHistory(username: String) {
        viewModelScope.launch {
            try {
                val response = api.getChatHistory(username)
                _chatMessages.value = response.chatList
            } catch (e: Exception) {
                e.printStackTrace()
                // Optional: Fehlerhandling (Snackbar etc.)
            }
        }
    }
}