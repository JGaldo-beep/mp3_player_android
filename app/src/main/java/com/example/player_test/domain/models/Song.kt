package com.example.player_test.domain.models

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val duration: Long, // in milliseconds
    val contentUri: Uri,
    val dateAdded: Long,
    val trackNumber: Int? = null,
    val size: Long = 0L,
    val mimeType: String = ""
) {
    fun getDurationString(): String {
        val seconds = duration / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%d:%02d".format(minutes, remainingSeconds)
    }
}