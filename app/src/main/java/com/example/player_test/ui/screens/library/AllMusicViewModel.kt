package com.example.player_test.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.models.Playlist
import com.example.player_test.domain.usecase.DeleteTracksUseCase
import com.example.player_test.domain.usecase.GetAllSongsUseCase
import com.example.player_test.domain.usecase.RefreshLibraryUseCase
import com.example.player_test.domain.usecase.ToggleFavoriteUseCase
import com.example.player_test.domain.usecase.playlist.GetAllPlaylistsUseCase
import com.example.player_test.domain.usecase.playlist.AddToPlaylistUseCase
import com.example.player_test.domain.repository.FavoritesRepository
import com.example.player_test.playback.PlayerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AllMusicViewModel(
    private val getAllSongsUseCase: GetAllSongsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteTracksUseCase: DeleteTracksUseCase,
    private val refreshLibraryUseCase: RefreshLibraryUseCase,
    private val favoritesRepository: FavoritesRepository,
    private val playerManager: PlayerManager,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val addToPlaylistUseCase: AddToPlaylistUseCase
) : ViewModel() {
    
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()
    
    private val _selectedSongs = MutableStateFlow<Set<Long>>(emptySet())
    val selectedSongs: StateFlow<Set<Long>> = _selectedSongs.asStateFlow()
    
    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _showAddToPlaylistDialog = MutableStateFlow(false)
    val showAddToPlaylistDialog: StateFlow<Boolean> = _showAddToPlaylistDialog.asStateFlow()
    
    private val _selectedSongForPlaylist = MutableStateFlow<Song?>(null)
    val selectedSongForPlaylist: StateFlow<Song?> = _selectedSongForPlaylist.asStateFlow()
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    
    init {
        observeSongs()
        observePlaylists()
    }
    
    private fun observeSongs() {
        viewModelScope.launch {
            getAllSongsUseCase().collect { songList ->
                _songs.value = songList
            }
        }
    }
    
    private fun observePlaylists() {
        viewModelScope.launch {
            getAllPlaylistsUseCase().collect { playlistList ->
                _playlists.value = playlistList
            }
        }
    }
    
    fun isFavorite(songId: Long): Flow<Boolean> {
        return favoritesRepository.isFavoriteFlow(songId)
    }
    
    fun toggleFavorite(songId: Long) {
        viewModelScope.launch {
            toggleFavoriteUseCase(songId)
        }
    }
    
    fun playSong(song: Song) {
        val songList = _songs.value
        val index = songList.indexOf(song)
        if (index >= 0) {
            playerManager.play(songList, index)
        }
    }
    
    fun playAll() {
        val songList = _songs.value
        if (songList.isNotEmpty()) {
            playerManager.play(songList, 0)
        }
    }
    
    fun enterSelectionMode(songId: Long) {
        _selectionMode.value = true
        _selectedSongs.value = setOf(songId)
    }
    
    fun exitSelectionMode() {
        _selectionMode.value = false
        _selectedSongs.value = emptySet()
    }
    
    fun toggleSongSelection(songId: Long) {
        val currentSelection = _selectedSongs.value
        _selectedSongs.value = if (currentSelection.contains(songId)) {
            currentSelection - songId
        } else {
            currentSelection + songId
        }
        
        // Exit selection mode if no songs are selected
        if (_selectedSongs.value.isEmpty()) {
            _selectionMode.value = false
        }
    }
    
    fun selectAllSongs() {
        _selectedSongs.value = _songs.value.map { it.id }.toSet()
    }
    
    fun deleteSelectedSongs() {
        viewModelScope.launch {
            val selectedIds = _selectedSongs.value.toList()
            if (selectedIds.isNotEmpty()) {
                _isLoading.value = true
                try {
                    deleteTracksUseCase(selectedIds)
                    exitSelectionMode()
                } catch (e: Exception) {
                    // Handle deletion error
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
    
    fun refreshLibrary() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                refreshLibraryUseCase()
            } catch (e: Exception) {
                // Handle refresh error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun getFilteredSongs(): List<Song> {
        val query = _searchQuery.value
        return if (query.isBlank()) {
            _songs.value
        } else {
            _songs.value.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                        song.artist.contains(query, ignoreCase = true) ||
                        song.album.contains(query, ignoreCase = true)
            }
        }
    }
    
    fun showAddToPlaylistDialog(song: Song) {
        _selectedSongForPlaylist.value = song
        _showAddToPlaylistDialog.value = true
    }
    
    fun hideAddToPlaylistDialog() {
        _showAddToPlaylistDialog.value = false
        _selectedSongForPlaylist.value = null
    }
    
    fun addSongToPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val song = _selectedSongForPlaylist.value
            if (song != null) {
                _isLoading.value = true
                try {
                    addToPlaylistUseCase(playlistId, listOf(song.id))
                    hideAddToPlaylistDialog()
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}