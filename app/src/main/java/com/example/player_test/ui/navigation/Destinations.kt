package com.example.player_test.ui.navigation

sealed class Destinations(val route: String) {
    object AllMusic : Destinations("all_music")
    object Playlists : Destinations("playlists")
    object Albums : Destinations("albums")
    object Favorites : Destinations("favorites")
    object NowPlaying : Destinations("now_playing")
    object AlbumDetail : Destinations("album_detail/{albumId}") {
        fun createRoute(albumId: Long) = "album_detail/$albumId"
    }
    object PlaylistDetail : Destinations("playlist_detail/{playlistId}") {
        fun createRoute(playlistId: Long) = "playlist_detail/$playlistId"
    }
    object Permissions : Destinations("permissions")
}