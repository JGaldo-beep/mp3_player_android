package com.example.player_test.data.media

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.player_test.data.local.entities.SongEntity
class MediaStoreScanner(
    private val context: Context
) {
    private val contentResolver: ContentResolver = context.contentResolver
    
    suspend fun scanAudioFiles(): List<SongEntity> {
        val songs = mutableListOf<SongEntity>()
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.MIME_TYPE
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val trackColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn) ?: "Unknown Title"
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val album = it.getString(albumColumn) ?: "Unknown Album"
                val albumId = it.getLong(albumIdColumn)
                val duration = it.getLong(durationColumn)
                val dateAdded = it.getLong(dateAddedColumn) * 1000 // Convert to milliseconds
                val track = it.getIntOrNull(trackColumn)
                val size = it.getLong(sizeColumn)
                val mimeType = it.getString(mimeTypeColumn) ?: ""
                
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                
                val song = SongEntity(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    albumId = albumId,
                    duration = duration,
                    contentUri = contentUri.toString(),
                    dateAdded = dateAdded,
                    trackNumber = track,
                    size = size,
                    mimeType = mimeType
                )
                
                songs.add(song)
            }
        }
        
        return songs
    }
    
    suspend fun deleteSongs(songIds: List<Long>): Int {
        var deletedCount = 0
        
        songIds.forEach { songId ->
            try {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    songId
                )
                
                val deleted = contentResolver.delete(uri, null, null)
                if (deleted > 0) {
                    deletedCount++
                }
            } catch (e: SecurityException) {
                // Handle permission denied for specific file
                // Continue with other files
            } catch (e: Exception) {
                // Handle other deletion errors
            }
        }
        
        return deletedCount
    }
    
    private fun Cursor.getIntOrNull(columnIndex: Int): Int? {
        return if (isNull(columnIndex)) null else getInt(columnIndex)
    }
}