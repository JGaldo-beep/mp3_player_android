package com.example.player_test.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.player_test.di.DependencyContainer
import com.example.player_test.ui.screens.library.AllMusicViewModel
import com.example.player_test.ui.screens.favorites.FavoritesViewModel
import com.example.player_test.ui.screens.playlists.PlaylistsViewModel
import com.example.player_test.ui.screens.playlists.PlaylistDetailViewModel

class ViewModelFactory(
    private val dependencyContainer: DependencyContainer,
    private val playlistId: Long? = null
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AllMusicViewModel::class.java -> AllMusicViewModel(
                dependencyContainer.getAllSongsUseCase,
                dependencyContainer.toggleFavoriteUseCase,
                dependencyContainer.deleteTracksUseCase,
                dependencyContainer.refreshLibraryUseCase,
                dependencyContainer.favoritesRepository,
                dependencyContainer.playerManager,
                dependencyContainer.getAllPlaylistsUseCase,
                dependencyContainer.addToPlaylistUseCase
            ) as T
            FavoritesViewModel::class.java -> FavoritesViewModel(
                dependencyContainer.getFavoritesUseCase,
                dependencyContainer.toggleFavoriteUseCase,
                dependencyContainer.favoritesRepository,
                dependencyContainer.playerManager
            ) as T
            PlaylistsViewModel::class.java -> PlaylistsViewModel(
                dependencyContainer.getAllPlaylistsUseCase,
                dependencyContainer.createPlaylistUseCase,
                dependencyContainer.updatePlaylistUseCase,
                dependencyContainer.deletePlaylistUseCase
            ) as T
            PlaylistDetailViewModel::class.java -> {
                requireNotNull(playlistId) { "PlaylistId is required for PlaylistDetailViewModel" }
                PlaylistDetailViewModel(
                    playlistId,
                    dependencyContainer.getPlaylistSongsUseCase,
                    dependencyContainer.removeFromPlaylistUseCase,
                    dependencyContainer.addToPlaylistUseCase,
                    dependencyContainer.getAllSongsUseCase,
                    dependencyContainer.playerManager
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}