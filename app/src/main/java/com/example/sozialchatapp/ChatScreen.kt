package com.example.sozialchatapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.sozialchatapp.network.GenerateRequest
import com.example.sozialchatapp.network.RetrofitClient
import com.example.sozialchatapp.models.ChatMessage
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    username: String = "guest",  // Username übergeben oder "guest"
    viewModel: ChatViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val chatHistory by viewModel.chatHistory.collectAsState()
    var userInput by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var pendingUserMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    // 💡 Chat-Verlauf beim ersten Laden holen
    LaunchedEffect(Unit) {
        viewModel.loadChatHistory(username)
    }

    // Automatisch scrollen bei neuer Nachricht
    LaunchedEffect(chatHistory.size) {
        delay(150)
        listState.animateScrollToItem(chatHistory.size)
        keyboardController?.hide()
    }

    // KI-Antwort anfordern bei neuer Benutzereingabe
    LaunchedEffect(pendingUserMessage) {
        val message = pendingUserMessage
        if (message != null) {
            isTyping = true
            try {
                val result = RetrofitClient.api.generateText(GenerateRequest(prompt = message, user = user))
                val reply = result.response // "Keine Antwort erhalten."
                viewModel.addBotMessage(reply) // ✅ Statt direktem .add()
            } catch (e: Exception) {
                viewModel.addBotMessage("Fehler: ${e.localizedMessage}")
            } finally {
                isTyping = false
                pendingUserMessage = null
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

        ChatHeader()

        LazyColumn(
            modifier = Modifier.weight(1f).padding(bottom = 16.dp),
            reverseLayout = false
        ) {
            items(chatHistory) { message ->
                ChatBubble(message = message)
            }
        }

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nachricht eingeben") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (userInput.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        viewModel.addUserMessage(username, userInput) // ✅ Über ViewModel
                        pendingUserMessage = userInput
                        userInput = ""
                        isLoading = false
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            enabled = !isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (isLoading) "..." else "Senden")
        }
    }
}


// ---------- Header ----------
@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Assistent Icon",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Assistent",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// ---------- Chatblasen ----------
@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    else
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)

    val textColor = MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(color = bubbleColor, shape = bubbleShape(message.isUser))
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(text = message.text, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.timestamp,
                color = textColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

// ---------- Form der Chatblasen ----------
fun bubbleShape(isUser: Boolean): Shape {
    return if (isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
    }
}