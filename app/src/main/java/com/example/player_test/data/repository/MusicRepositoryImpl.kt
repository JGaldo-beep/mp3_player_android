package com.example.player_test.data.repository

import android.content.Context
import android.net.Uri
import com.example.player_test.data.local.dao.SongDao
import com.example.player_test.data.local.entities.SongEntity
import com.example.player_test.data.media.MediaStoreScanner
import com.example.player_test.domain.models.Album
import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class MusicRepositoryImpl(
    private val songDao: SongDao,
    private val mediaStoreScanner: MediaStoreScanner,
    private val context: Context
) : MusicRepository {
    
    override fun getAllSongs(): Flow<List<Song>> {
        return songDao.getAllSongs().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getSongById(id: Long): Song? {
        return songDao.getSongById(id)?.toDomainModel()
    }
    
    override fun getSongsByAlbum(albumId: Long): Flow<List<Song>> {
        return songDao.getSongsByAlbum(albumId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllAlbums(): Flow<List<Album>> {
        return songDao.getAllAlbums().map { albumInfos ->
            albumInfos.map { albumInfo ->
                Album(
                    albumId = albumInfo.albumId,
                    name = albumInfo.album,
                    artist = albumInfo.artist,
                    songCount = albumInfo.songCount,
                    albumArt = getAlbumArtUri(albumInfo.albumId)
                )
            }
        }
    }
    
    override fun searchSongs(query: String): Flow<List<Song>> {
        return songDao.searchSongs(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun refreshLibrary() {
        try {
            val songs = mediaStoreScanner.scanAudioFiles()
            songDao.deleteAllSongs()
            songDao.insertSongs(songs)
        } catch (e: Exception) {
            // Handle scan error
        }
    }
    
    override suspend fun deleteSongs(songIds: List<Long>): Result<Unit> {
        return try {
            // First attempt to delete from MediaStore
            val deletedCount = mediaStoreScanner.deleteSongs(songIds)
            
            // Remove from local database (regardless of MediaStore deletion result)
            songDao.deleteSongs(songIds)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
    
    private fun getAlbumArtUri(albumId: Long): Uri? {
        return try {
            Uri.parse("content://media/external/audio/albumart/$albumId")
        } catch (e: Exception) {
            null
        }
    }
}