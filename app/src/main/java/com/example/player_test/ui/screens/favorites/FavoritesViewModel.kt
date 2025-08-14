package com.example.player_test.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.usecase.GetFavoritesUseCase
import com.example.player_test.domain.usecase.ToggleFavoriteUseCase
import com.example.player_test.domain.repository.FavoritesRepository
import com.example.player_test.playback.PlayerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoritesRepository: FavoritesRepository,
    private val playerManager: PlayerManager
) : ViewModel() {
    
    private val _favorites = MutableStateFlow<List<Song>>(emptyList())
    val favorites: StateFlow<List<Song>> = _favorites.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        observeFavorites()
    }
    
    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favoritesList ->
                _favorites.value = favoritesList
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
        val songList = _favorites.value
        val index = songList.indexOf(song)
        if (index >= 0) {
            playerManager.play(songList, index)
        }
    }
    
    fun playAll() {
        val songList = _favorites.value
        if (songList.isNotEmpty()) {
            playerManager.play(songList, 0)
        }
    }
}