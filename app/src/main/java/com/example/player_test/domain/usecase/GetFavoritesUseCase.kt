package com.example.player_test.domain.usecase

import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
class GetFavoritesUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<Song>> {
        return favoritesRepository.getFavoriteSongs()
    }
}