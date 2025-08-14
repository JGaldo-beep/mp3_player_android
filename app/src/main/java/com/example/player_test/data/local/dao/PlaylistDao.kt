package com.example.player_test.data.local.dao

import androidx.room.*
import com.example.player_test.data.local.entities.PlaylistEntity
import com.example.player_test.data.local.entities.PlaylistSongCrossRef
import com.example.player_test.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?
    
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long
    
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)
    
    @Query("UPDATE playlists SET name = :newName WHERE id = :playlistId")
    suspend fun updatePlaylistName(playlistId: Long, newName: String)
    
    // Playlist-Song relationship operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSong(crossRef: PlaylistSongCrossRef)
    
    @Delete
    suspend fun deletePlaylistSong(crossRef: PlaylistSongCrossRef)
    
    @Query("DELETE FROM playlist_song_cross_ref WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removePlaylistSong(playlistId: Long, songId: Long)
    
    @Query("SELECT songs.* FROM songs INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId WHERE playlist_song_cross_ref.playlistId = :playlistId ORDER BY playlist_song_cross_ref.position ASC")
    fun getPlaylistSongs(playlistId: Long): Flow<List<SongEntity>>
    
    @Query("SELECT MAX(position) FROM playlist_song_cross_ref WHERE playlistId = :playlistId")
    suspend fun getMaxPosition(playlistId: Long): Int?
    
    @Query("UPDATE playlist_song_cross_ref SET position = :newPosition WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun updateSongPosition(playlistId: Long, songId: Long, newPosition: Int)
    
    @Transaction
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        val maxPosition = getMaxPosition(playlistId) ?: -1
        insertPlaylistSong(PlaylistSongCrossRef(playlistId, songId, maxPosition + 1))
    }
    
    @Transaction
    suspend fun reorderPlaylistSongs(playlistId: Long, songPositions: List<Pair<Long, Int>>) {
        songPositions.forEach { (songId, position) ->
            updateSongPosition(playlistId, songId, position)
        }
    }
}