package com.example.sozialchatapp.network

// Für den Request an Ollama
data class GenerateRequest(
    val model: String = "tinyllama", // KI Model wählen
    val prompt: String,
    val user: String,
    val stream: Boolean = false
)