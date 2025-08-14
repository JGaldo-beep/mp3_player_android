package com.example.player_test.domain.models

data class Playlist(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val songs: List<Song> = emptyList()
) {
    val songCount: Int get() = songs.size
    val totalDuration: Long get() = songs.sumOf { it.duration }
    
    fun getTotalDurationString(): String {
        val seconds = totalDuration / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, seconds % 60)
        } else {
            "%d:%02d".format(minutes, seconds % 60)
        }
    }
}