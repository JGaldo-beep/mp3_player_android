package com.example.player_test.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.player_test.data.local.dao.FavoriteDao
import com.example.player_test.data.local.dao.PlaylistDao
import com.example.player_test.data.local.dao.SongDao
import com.example.player_test.data.local.entities.FavoriteEntity
import com.example.player_test.data.local.entities.PlaylistEntity
import com.example.player_test.data.local.entities.PlaylistSongCrossRef
import com.example.player_test.data.local.entities.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoriteDao(): FavoriteDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mp3_player_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}