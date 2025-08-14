package com.example.player_test.domain.repository

import com.example.player_test.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteSongs(): Flow<List<Song>>
    suspend fun isFavorite(songId: Long): Boolean
    fun isFavoriteFlow(songId: Long): Flow<Boolean>
    suspend fun toggleFavorite(songId: Long)
}