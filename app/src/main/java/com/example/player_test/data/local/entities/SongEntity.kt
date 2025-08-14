package com.example.player_test.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Long, // MediaStore _ID
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val duration: Long, // in milliseconds
    val contentUri: String, // MediaStore content URI
    val dateAdded: Long, // timestamp
    val trackNumber: Int? = null,
    val size: Long = 0L,
    val mimeType: String = ""
)