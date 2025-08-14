package com.example.player_test.data.repository

import android.net.Uri
import com.example.player_test.data.local.dao.PlaylistDao
import com.example.player_test.data.local.entities.PlaylistEntity
import com.example.player_test.data.local.entities.SongEntity
import com.example.player_test.domain.models.Playlist
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    
    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getPlaylistById(id: Long): Playlist? {
        return playlistDao.getPlaylistById(id)?.toDomainModel()
    }
    
    override fun getPlaylistSongs(playlistId: Long): Flow<List<Song>> {
        return playlistDao.getPlaylistSongs(playlistId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun createPlaylist(name: String): Long {
        val playlist = PlaylistEntity(name = name)
        return playlistDao.insertPlaylist(playlist)
    }
    
    override suspend fun updatePlaylist(playlist: Playlist) {
        val entity = PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            createdAt = playlist.createdAt
        )
        playlistDao.updatePlaylist(entity)
    }
    
    override suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylistById(playlistId)
    }
    
    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        playlistDao.addSongToPlaylist(playlistId, songId)
    }
    
    override suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>) {
        songIds.forEach { songId ->
            playlistDao.addSongToPlaylist(playlistId, songId)
        }
    }
    
    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removePlaylistSong(playlistId, songId)
    }
    
    override suspend fun updatePlaylistName(playlistId: Long, newName: String) {
        playlistDao.updatePlaylistName(playlistId, newName)
    }
    
    override suspend fun reorderPlaylistSongs(playlistId: Long, songPositions: List<Pair<Long, Int>>) {
        playlistDao.reorderPlaylistSongs(playlistId, songPositions)
    }
    
    private fun PlaylistEntity.toDomainModel(): Playlist {
        return Playlist(
            id = id,
            name = name,
            createdAt = createdAt
        )
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