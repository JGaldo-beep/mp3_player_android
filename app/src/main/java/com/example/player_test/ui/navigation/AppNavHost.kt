package com.example.player_test.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.player_test.ui.screens.albums.AlbumDetailScreen
import com.example.player_test.ui.screens.albums.AlbumsScreen
import com.example.player_test.ui.screens.favorites.FavoritesScreen
import com.example.player_test.ui.screens.library.AllMusicScreen
import com.example.player_test.ui.screens.player.NowPlayingScreen
import com.example.player_test.ui.screens.playlists.PlaylistDetailScreen
import com.example.player_test.ui.screens.playlists.PlaylistsScreen
import com.example.player_test.ui.screens.permissions.PermissionsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Destinations.AllMusic.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.AllMusic.route) {
            AllMusicScreen(navController = navController)
        }
        
        composable(Destinations.Playlists.route) {
            PlaylistsScreen(navController = navController)
        }
        
        composable(Destinations.Albums.route) {
            AlbumsScreen(navController = navController)
        }
        
        composable(Destinations.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        
        composable(Destinations.NowPlaying.route) {
            NowPlayingScreen(navController = navController)
        }
        
        composable(
            route = Destinations.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0L
            AlbumDetailScreen(
                albumId = albumId,
                navController = navController
            )
        }
        
        composable(
            route = Destinations.PlaylistDetail.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            PlaylistDetailScreen(
                playlistId = playlistId,
                navController = navController
            )
        }
        
        composable(Destinations.Permissions.route) {
            PermissionsScreen(navController = navController)
        }
    }
}