package com.example.player_test.ui.screens.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player_test.domain.models.Playlist
import com.example.player_test.domain.usecase.playlist.CreatePlaylistUseCase
import com.example.player_test.domain.usecase.playlist.DeletePlaylistUseCase
import com.example.player_test.domain.usecase.playlist.GetAllPlaylistsUseCase
import com.example.player_test.domain.usecase.playlist.UpdatePlaylistUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()
    
    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()
    
    private val _editingPlaylist = MutableStateFlow<Playlist?>(null)
    val editingPlaylist: StateFlow<Playlist?> = _editingPlaylist.asStateFlow()
    
    init {
        observePlaylists()
    }
    
    private fun observePlaylists() {
        viewModelScope.launch {
            getAllPlaylistsUseCase().collect { playlistList ->
                _playlists.value = playlistList
            }
        }
    }
    
    fun showCreateDialog() {
        _showCreateDialog.value = true
    }
    
    fun hideCreateDialog() {
        _showCreateDialog.value = false
    }
    
    fun showEditDialog(playlist: Playlist) {
        _editingPlaylist.value = playlist
        _showEditDialog.value = true
    }
    
    fun hideEditDialog() {
        _showEditDialog.value = false
        _editingPlaylist.value = null
    }
    
    fun createPlaylist(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                createPlaylistUseCase(name.trim())
                hideCreateDialog()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updatePlaylist(playlistId: Long, newName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                updatePlaylistUseCase(playlistId, newName.trim())
                hideEditDialog()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                deletePlaylistUseCase(playlistId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}