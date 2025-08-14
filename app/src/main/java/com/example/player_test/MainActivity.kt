package com.example.player_test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.player_test.playback.PlayerManager
import com.example.player_test.ui.components.BottomNavigationBar
import com.example.player_test.ui.components.MiniPlayerBar
import com.example.player_test.ui.navigation.AppNavHost
import com.example.player_test.ui.navigation.Destinations
import com.example.player_test.ui.theme.Player_testTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var playerManager: PlayerManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as Mp3PlayerApplication
        playerManager = app.dependencyContainer.playerManager
        
        setContent {
            Player_testTheme {
                Mp3PlayerApp()
            }
        }
    }
    
    @Composable
    fun Mp3PlayerApp() {
        val navController = rememberNavController()
        val context = LocalContext.current
        
        // Check permissions
        val hasPermission = remember {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        
        val startDestination = if (hasPermission) {
            Destinations.AllMusic.route
        } else {
            Destinations.Permissions.route
        }
        
        // Track current route to hide mini player on Now Playing screen
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val isNowPlayingScreen = currentRoute == Destinations.NowPlaying.route
        
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            bottomBar = {
                Column {
                    // Mini player (shown when a song is playing, but hidden on Now Playing screen)
                    if (!isNowPlayingScreen) {
                        MiniPlayerBar(
                            playerManager = playerManager,
                            onExpandClick = {
                                navController.navigate(Destinations.NowPlaying.route)
                            }
                        )
                    }
                    
                    // Bottom navigation (only show if permissions granted)
                    if (hasPermission) {
                        BottomNavigationBar(navController = navController)
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}