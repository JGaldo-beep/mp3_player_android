package com.example.player_test.domain.repository

import com.example.player_test.domain.models.Playlist
import com.example.player_test.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(id: Long): Playlist?
    fun getPlaylistSongs(playlistId: Long): Flow<List<Song>>
    suspend fun createPlaylist(name: String): Long
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>)
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    suspend fun updatePlaylistName(playlistId: Long, newName: String)
    suspend fun reorderPlaylistSongs(playlistId: Long, songPositions: List<Pair<Long, Int>>)
}