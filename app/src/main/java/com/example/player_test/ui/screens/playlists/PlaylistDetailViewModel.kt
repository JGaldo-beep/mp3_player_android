package com.example.player_test.ui.screens.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.usecase.GetAllSongsUseCase
import com.example.player_test.domain.usecase.playlist.AddToPlaylistUseCase
import com.example.player_test.domain.usecase.playlist.GetPlaylistSongsUseCase
import com.example.player_test.domain.usecase.playlist.RemoveFromPlaylistUseCase
import com.example.player_test.playback.PlayerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistId: Long,
    private val getPlaylistSongsUseCase: GetPlaylistSongsUseCase,
    private val removeFromPlaylistUseCase: RemoveFromPlaylistUseCase,
    private val addToPlaylistUseCase: AddToPlaylistUseCase,
    private val getAllSongsUseCase: GetAllSongsUseCase,
    private val playerManager: PlayerManager
) : ViewModel() {
    
    private val _playlistSongs = MutableStateFlow<List<Song>>(emptyList())
    val playlistSongs: StateFlow<List<Song>> = _playlistSongs.asStateFlow()
    
    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())
    val allSongs: StateFlow<List<Song>> = _allSongs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _showAddSongsDialog = MutableStateFlow(false)
    val showAddSongsDialog: StateFlow<Boolean> = _showAddSongsDialog.asStateFlow()
    
    private val _selectedSongsToAdd = MutableStateFlow<Set<Long>>(emptySet())
    val selectedSongsToAdd: StateFlow<Set<Long>> = _selectedSongsToAdd.asStateFlow()
    
    init {
        observePlaylistSongs()
        observeAllSongs()
    }
    
    private fun observePlaylistSongs() {
        viewModelScope.launch {
            getPlaylistSongsUseCase(playlistId).collect { songs ->
                _playlistSongs.value = songs
            }
        }
    }
    
    private fun observeAllSongs() {
        viewModelScope.launch {
            getAllSongsUseCase().collect { songs ->
                _allSongs.value = songs
            }
        }
    }
    
    fun playSong(song: Song) {
        val songList = _playlistSongs.value
        val index = songList.indexOf(song)
        if (index >= 0) {
            playerManager.play(songList, index)
        }
    }
    
    fun playAll() {
        val songList = _playlistSongs.value
        if (songList.isNotEmpty()) {
            playerManager.play(songList, 0)
        }
    }
    
    fun removeSongFromPlaylist(songId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                removeFromPlaylistUseCase(playlistId, songId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun showAddSongsDialog() {
        _showAddSongsDialog.value = true
        _selectedSongsToAdd.value = emptySet()
    }
    
    fun hideAddSongsDialog() {
        _showAddSongsDialog.value = false
        _selectedSongsToAdd.value = emptySet()
    }
    
    fun toggleSongSelection(songId: Long) {
        val currentSelection = _selectedSongsToAdd.value
        _selectedSongsToAdd.value = if (currentSelection.contains(songId)) {
            currentSelection - songId
        } else {
            currentSelection + songId
        }
    }
    
    fun addSelectedSongsToPlaylist() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val selectedIds = _selectedSongsToAdd.value.toList()
                if (selectedIds.isNotEmpty()) {
                    addToPlaylistUseCase(playlistId, selectedIds)
                    hideAddSongsDialog()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getAvailableSongs(): List<Song> {
        val playlistSongIds = _playlistSongs.value.map { it.id }.toSet()
        return _allSongs.value.filter { it.id !in playlistSongIds }
    }
}