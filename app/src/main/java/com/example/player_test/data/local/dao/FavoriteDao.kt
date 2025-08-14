package com.example.player_test.data.local.dao

import androidx.room.*
import com.example.player_test.data.local.entities.FavoriteEntity
import com.example.player_test.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    
    @Query("SELECT songs.* FROM songs INNER JOIN favorites ON songs.id = favorites.songId ORDER BY favorites.addedAt DESC")
    fun getFavoriteSongs(): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM favorites WHERE songId = :songId")
    suspend fun getFavorite(songId: Long): FavoriteEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE songId = :songId)")
    suspend fun isFavorite(songId: Long): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE songId = :songId)")
    fun isFavoriteFlow(songId: Long): Flow<Boolean>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)
    
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
    
    @Query("DELETE FROM favorites WHERE songId = :songId")
    suspend fun deleteFavoriteBySongId(songId: Long)
    
    @Transaction
    suspend fun toggleFavorite(songId: Long) {
        val favorite = getFavorite(songId)
        if (favorite != null) {
            deleteFavorite(favorite)
        } else {
            insertFavorite(FavoriteEntity(songId))
        }
    }
}