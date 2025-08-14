package com.example.player_test.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.player_test.Mp3PlayerApplication
import com.example.player_test.ui.ViewModelFactory
import com.example.player_test.ui.components.FastScrollLazyColumn
import com.example.player_test.ui.components.SongRow
import com.example.player_test.ui.theme.gradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMusicScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val app = context.applicationContext as Mp3PlayerApplication
    val viewModel: AllMusicViewModel = viewModel(
        factory = ViewModelFactory(app.dependencyContainer)
    )
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val selectedSongs by viewModel.selectedSongs.collectAsStateWithLifecycle()
    val selectionMode by viewModel.selectionMode.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val showAddToPlaylistDialog by viewModel.showAddToPlaylistDialog.collectAsStateWithLifecycle()
    val selectedSongForPlaylist by viewModel.selectedSongForPlaylist.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    
    val filteredSongs = remember(songs, searchQuery) {
        viewModel.getFilteredSongs()
    }
    
    val listState = rememberLazyListState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var searchExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (selectionMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.exitSelectionMode() }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Exit Selection",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                            }
                            Text(
                                text = "${selectedSongs.size} selected",
                                style = MaterialTheme.typography.headlineSmall,
                                color = androidx.compose.ui.graphics.Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row {
                            IconButton(onClick = { viewModel.selectAllSongs() }) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Select All",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                            }
                            IconButton(
                                onClick = { showDeleteDialog = true },
                                enabled = selectedSongs.isNotEmpty()
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Selected",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "All Music",
                        style = MaterialTheme.typography.headlineSmall,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = { searchExpanded = !searchExpanded }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        IconButton(onClick = { viewModel.playAll() }) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play All",
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        IconButton(onClick = { viewModel.refreshLibrary() }) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Refresh Library",
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        IconButton(onClick = { 
                            if (songs.isNotEmpty()) {
                                viewModel.enterSelectionMode(songs.first().id)
                            }
                        }) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Select Multiple",
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            }
        }
        
        // Search field
        if (searchExpanded) {
            TextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                label = { Text("Search songs...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )
        }
        
        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
        
        // Songs list with fast scroll
        FastScrollLazyColumn(
            items = filteredSongs,
            listState = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            keySelector = { it.id },
            indexSelector = { song -> 
                song.title.firstOrNull()?.toString()?.uppercase() ?: "#"
            }
        ) {
            items(filteredSongs, key = { it.id }) { song ->
                val isFavorite by viewModel.isFavorite(song.id).collectAsStateWithLifecycle(initialValue = false)
                val isSelected = selectedSongs.contains(song.id)
                
                SongRow(
                    song = song,
                    isFavorite = isFavorite,
                    isSelected = isSelected,
                    selectionMode = selectionMode,
                    onClick = { viewModel.playSong(song) },
                    onLongClick = { 
                        if (!selectionMode) {
                            viewModel.showAddToPlaylistDialog(song)
                        }
                    },
                    onFavoriteClick = { viewModel.toggleFavorite(song.id) },
                    onSelectionClick = { viewModel.toggleSongSelection(song.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Songs") },
            text = { 
                Text("Delete ${selectedSongs.size} songs? This will remove files from your device.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSelectedSongs()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Add to Playlist Dialog
    if (showAddToPlaylistDialog && selectedSongForPlaylist != null) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddToPlaylistDialog() },
            title = { Text("Add to Playlist") },
            text = {
                if (playlists.isEmpty()) {
                    Text("No playlists available. Create a playlist first.")
                } else {
                    Column {
                        Text(
                            text = "Add \"${selectedSongForPlaylist?.title ?: ""}\" to:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        LazyColumn(
                            modifier = Modifier.height(200.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(playlists, key = { it.id }) { playlist ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            viewModel.addSongToPlaylist(playlist.id)
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        text = playlist.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.hideAddToPlaylistDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Handle back navigation in selection mode
    LaunchedEffect(selectionMode) {
        // You might want to handle back press here to exit selection mode
    }
}