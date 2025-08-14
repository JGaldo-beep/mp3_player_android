package com.example.player_test.domain.usecase

import com.example.player_test.domain.repository.FavoritesRepository
class ToggleFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(songId: Long) {
        favoritesRepository.toggleFavorite(songId)
    }
}