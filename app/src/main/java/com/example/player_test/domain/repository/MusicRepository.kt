package com.example.player_test.domain.repository

import com.example.player_test.domain.models.Album
import com.example.player_test.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getAllSongs(): Flow<List<Song>>
    suspend fun getSongById(id: Long): Song?
    fun getSongsByAlbum(albumId: Long): Flow<List<Song>>
    fun getAllAlbums(): Flow<List<Album>>
    fun searchSongs(query: String): Flow<List<Song>>
    suspend fun refreshLibrary()
    suspend fun deleteSongs(songIds: List<Long>): Result<Unit>
}