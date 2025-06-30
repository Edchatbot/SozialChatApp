package com.example.sozialchatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NewsScreen() {
    var expandedSection by remember { mutableStateOf("Mein Feed") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Aktuelles Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Aktuelle Nachrichten",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Themenbereiche
        NewsSection(
            title = "Bund",
            isExpanded = expandedSection == "Bund",
            onClick = { expandedSection =  if (expandedSection == "Bund") "" else "Bund"  }
        )

        NewsSection(
            title = "Land",
            isExpanded = expandedSection == "Land",
            onClick = { expandedSection =  if (expandedSection == "Land") "" else "Land"  }
        )

        NewsSection(
            title = "Gemeinde",
            isExpanded = expandedSection == "Gemeinde",
            onClick = { expandedSection =  if (expandedSection == "Gemeinde") "" else "Gemeinde"  }
        )

        NewsSection(
            title = "Mein Feed",
            isExpanded = expandedSection == "Mein Feed",
            onClick = { expandedSection = if (expandedSection == "Mein Feed") "" else "Mein Feed" }
        )
    }
}

@Composable
fun NewsSection(
    title: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                // Hier kannst du echte Nachrichten einbinden
                Text(
                    text = "Hier erscheinen aktuelle Inhalte für \"$title\"...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}