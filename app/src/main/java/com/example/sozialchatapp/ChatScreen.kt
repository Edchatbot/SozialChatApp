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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.*
import androidx.compose.foundation.layout.padding
import com.example.sozialchatapp.network.GenerateRequest
import com.example.sozialchatapp.network.RetrofitClient
import com.example.sozialchatapp.models.ChatEntry


@Composable
fun ChatScreen() {
    var userInput by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var pendingUserMessage by remember { mutableStateOf<String?>(null) }

    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    // Scroll bei neuer Nachricht
    LaunchedEffect(chatMessages.size) {
        delay(150)
        listState.animateScrollToItem(chatMessages.size)
        keyboardController?.hide()
    }

    // KI-Antwort über Retrofit
    LaunchedEffect(pendingUserMessage) {
        val message = pendingUserMessage
        if (message != null) {
            isTyping = true

            try {
                val result = RetrofitClient.api.generateText(GenerateRequest(prompt = message))

                if (result.response != null) {
                    chatMessages.add(ChatMessage(text = result.response, isUser = false))
                } else {
                    chatMessages.add(ChatMessage(text = "Keine Antwort erhalten.", isUser = false))
                }
            } catch (e: Exception) {
                chatMessages.add(ChatMessage(text = "Fehler: ${e.localizedMessage}", isUser = false))
            } finally {
                isTyping = false
                pendingUserMessage = null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
        //.padding(imePadding)
        //.padding(bottom = 8.dp)
    ) {
        ChatHeader()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                //.padding(imePadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(chatMessages) { msg ->
                ChatBubble(msg)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (isTyping) {
                item {
                    ChatBubble(ChatMessage(text = "Assistent schreibt...", isUser = false))
                }
            }
        }

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Deine Nachricht...") },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(12.dp), // optional für weiche Ecken
            )

            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (userInput.isNotBlank()) {
                    val msg = userInput
                    chatMessages.add(ChatMessage(text = msg, isUser = true))
                    userInput = ""
                    pendingUserMessage = msg
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            ) {
                Text("Senden")
            }
        }
    }
}


@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //
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






@Composable
fun ChatHistoryScreen(viewModel: ChatViewModel = viewModel()) {
    val chatList by viewModel.chatHistory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChatHistory()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(chatList) { chat ->
            ChatItem(chat)
        }
    }
}

@Composable
fun ChatItem(chat: ChatEntry) {
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {

        Text("🧑 Prompt:", style = MaterialTheme.typography.labelLarge)
        Text(chat.prompt)

        Spacer(modifier = Modifier.height(4.dp))

        Text("🤖 Antwort:", style = MaterialTheme.typography.labelLarge)
        Text(chat.response)

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}





//Chatbubbles
@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
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

fun bubbleShape(isUser: Boolean): Shape {
    return if (isUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    }
}