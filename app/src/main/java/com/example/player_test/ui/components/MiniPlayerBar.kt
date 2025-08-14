package com.example.player_test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player_test.playback.PlayerManager
import com.example.player_test.ui.theme.gradientBrush

@Composable
fun MiniPlayerBar(
    playerManager: PlayerManager,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSong by playerManager.currentSong.collectAsStateWithLifecycle()
    val isPlaying by playerManager.isPlaying.collectAsStateWithLifecycle()
    
    currentSong?.let { song ->
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onExpandClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush())
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Song info - constrained width to prevent control overflow
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = song.title.take(50) + if (song.title.length > 50) "..." else "",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = song.artist.take(40) + if (song.artist.length > 40) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Control buttons - fixed width container
                Row(
                    modifier = Modifier.width(144.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    IconButton(
                        onClick = { playerManager.playPrevious() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            text = "⏮",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    // Play/Pause Button - Larger and more prominent
                    IconButton(
                        onClick = { playerManager.playPause() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Text(
                            text = if (isPlaying) "⏸" else "▶️",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    // Next Button
                    IconButton(
                        onClick = { playerManager.playNext() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            text = "⏭",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}