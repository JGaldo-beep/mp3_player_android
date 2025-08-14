package com.example.player_test.data.repository

import android.net.Uri
import com.example.player_test.data.local.dao.FavoriteDao
import com.example.player_test.data.local.entities.SongEntity
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class FavoritesRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoritesRepository {
    
    override fun getFavoriteSongs(): Flow<List<Song>> {
        return favoriteDao.getFavoriteSongs().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun isFavorite(songId: Long): Boolean {
        return favoriteDao.isFavorite(songId)
    }
    
    override fun isFavoriteFlow(songId: Long): Flow<Boolean> {
        return favoriteDao.isFavoriteFlow(songId)
    }
    
    override suspend fun toggleFavorite(songId: Long) {
        favoriteDao.toggleFavorite(songId)
    }
    
    private fun SongEntity.toDomainModel(): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            album = album,
            albumId = albumId,
            duration = duration,
            contentUri = Uri.parse(contentUri),
            dateAdded = dateAdded,
            trackNumber = trackNumber,
            size = size,
            mimeType = mimeType
        )
    }
}