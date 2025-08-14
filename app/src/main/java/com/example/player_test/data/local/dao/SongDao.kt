package com.example.player_test.data.local.dao

import androidx.room.*
import com.example.player_test.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: Long): SongEntity?
    
    @Query("SELECT * FROM songs WHERE albumId = :albumId ORDER BY trackNumber ASC, title ASC")
    fun getSongsByAlbum(albumId: Long): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<SongEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)
    
    @Delete
    suspend fun deleteSong(song: SongEntity)
    
    @Query("DELETE FROM songs WHERE id IN (:songIds)")
    suspend fun deleteSongs(songIds: List<Long>)
    
    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
    
    @Query("SELECT DISTINCT album, albumId, artist, COUNT(*) as songCount FROM songs WHERE albumId = :albumId GROUP BY albumId")
    suspend fun getAlbumInfo(albumId: Long): AlbumInfo?
    
    @Query("SELECT DISTINCT album, albumId, artist, COUNT(*) as songCount FROM songs GROUP BY albumId ORDER BY album ASC")
    fun getAllAlbums(): Flow<List<AlbumInfo>>
}

data class AlbumInfo(
    val album: String,
    val albumId: Long,
    val artist: String,
    val songCount: Int
)