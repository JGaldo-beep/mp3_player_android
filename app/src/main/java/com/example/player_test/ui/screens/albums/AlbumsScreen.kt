package com.example.player_test.ui.screens.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.player_test.ui.theme.gradientBrush

@Composable
fun AlbumsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush())
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Albums",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Albums screen - Coming soon",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun AlbumDetailScreen(albumId: Long, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Album Detail - ID: $albumId",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}