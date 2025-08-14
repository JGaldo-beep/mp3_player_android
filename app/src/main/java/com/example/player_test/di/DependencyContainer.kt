package com.example.player_test.di

import android.content.Context
import androidx.room.Room
import com.example.player_test.data.local.dao.FavoriteDao
import com.example.player_test.data.local.dao.PlaylistDao
import com.example.player_test.data.local.dao.SongDao
import com.example.player_test.data.local.db.AppDatabase
import com.example.player_test.data.media.MediaStoreScanner
import com.example.player_test.data.repository.FavoritesRepositoryImpl
import com.example.player_test.data.repository.MusicRepositoryImpl
import com.example.player_test.data.repository.PlaylistRepositoryImpl
import com.example.player_test.domain.repository.FavoritesRepository
import com.example.player_test.domain.repository.MusicRepository
import com.example.player_test.domain.repository.PlaylistRepository
import com.example.player_test.domain.usecase.*
import com.example.player_test.domain.usecase.playlist.AddToPlaylistUseCase
import com.example.player_test.domain.usecase.playlist.CreatePlaylistUseCase
import com.example.player_test.domain.usecase.playlist.DeletePlaylistUseCase
import com.example.player_test.domain.usecase.playlist.GetAllPlaylistsUseCase
import com.example.player_test.domain.usecase.playlist.GetPlaylistSongsUseCase
import com.example.player_test.domain.usecase.playlist.RemoveFromPlaylistUseCase
import com.example.player_test.domain.usecase.playlist.UpdatePlaylistUseCase
import com.example.player_test.playback.PlayerManager

class DependencyContainer(private val context: Context) {
    
    // Database
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mp3_player_database"
        ).build()
    }
    
    // DAOs
    val songDao: SongDao by lazy { database.songDao() }
    val playlistDao: PlaylistDao by lazy { database.playlistDao() }
    val favoriteDao: FavoriteDao by lazy { database.favoriteDao() }
    
    // Media Scanner
    val mediaStoreScanner: MediaStoreScanner by lazy {
        MediaStoreScanner(context)
    }
    
    // Repositories
    val musicRepository: MusicRepository by lazy {
        MusicRepositoryImpl(songDao, mediaStoreScanner, context)
    }
    
    val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepositoryImpl(playlistDao)
    }
    
    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepositoryImpl(favoriteDao)
    }
    
    // Player Manager
    val playerManager: PlayerManager by lazy {
        PlayerManager(context)
    }
    
    // Use Cases
    val getAllSongsUseCase: GetAllSongsUseCase by lazy {
        GetAllSongsUseCase(musicRepository)
    }
    
    val getAlbumsUseCase: GetAlbumsUseCase by lazy {
        GetAlbumsUseCase(musicRepository)
    }
    
    val getFavoritesUseCase: GetFavoritesUseCase by lazy {
        GetFavoritesUseCase(favoritesRepository)
    }
    
    val toggleFavoriteUseCase: ToggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(favoritesRepository)
    }
    
    val deleteTracksUseCase: DeleteTracksUseCase by lazy {
        DeleteTracksUseCase(musicRepository)
    }
    
    val refreshLibraryUseCase: RefreshLibraryUseCase by lazy {
        RefreshLibraryUseCase(musicRepository)
    }
    
    val getAllPlaylistsUseCase: GetAllPlaylistsUseCase by lazy {
        GetAllPlaylistsUseCase(playlistRepository)
    }
    
    val createPlaylistUseCase: CreatePlaylistUseCase by lazy {
        CreatePlaylistUseCase(playlistRepository)
    }
    
    val updatePlaylistUseCase: UpdatePlaylistUseCase by lazy {
        UpdatePlaylistUseCase(playlistRepository)
    }
    
    val deletePlaylistUseCase: DeletePlaylistUseCase by lazy {
        DeletePlaylistUseCase(playlistRepository)
    }
    
    val addToPlaylistUseCase: AddToPlaylistUseCase by lazy {
        AddToPlaylistUseCase(playlistRepository)
    }
    
    val removeFromPlaylistUseCase: RemoveFromPlaylistUseCase by lazy {
        RemoveFromPlaylistUseCase(playlistRepository)
    }
    
    val getPlaylistSongsUseCase: GetPlaylistSongsUseCase by lazy {
        GetPlaylistSongsUseCase(playlistRepository)
    }
}