package com.example.player_test.domain.models

import android.net.Uri

data class Album(
    val albumId: Long,
    val name: String,
    val artist: String,
    val songCount: Int,
    val albumArt: Uri? = null
)