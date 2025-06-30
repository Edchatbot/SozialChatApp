package com.example.sozialchatapp

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Inhalt z.B.: Nachrichten
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 48.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Aktuelle Nachrichten",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Nur hier sichtbar: die IconNavigationBar
        IconNavigationBar(navController)
    }
}

@Composable
fun IconNavigationBar(navController: NavController) {
    val allIcons = listOf(
        Triple(Icons.AutoMirrored.Filled.Chat, "Chat", "chat"),
        Triple(Icons.Filled.Newspaper, "Aktuelles", "news"),
        Triple(Icons.Filled.CalendarViewMonth, "Termin", "termin"),
        Triple(Icons.Filled.Contacts, "Kontakte", "kontakte"),
        Triple(Icons.Filled.Info, "Info 1", "info1"),
        Triple(Icons.Filled.Info, "Info 2", "info2"),
        Triple(Icons.Filled.Info, "Info 3", "info3"),
        Triple(Icons.Filled.Info, "Info 4", "info4"),
    )

    // Größe der einzelnen Icons + Abstand = ~ 1/4 des Bildschirms
    val iconWidth = 80.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        allIcons.forEach { (icon, description, route) ->
            Box(
                modifier = Modifier.width(iconWidth),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}