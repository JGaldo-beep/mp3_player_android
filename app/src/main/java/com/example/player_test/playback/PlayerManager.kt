package com.example.player_test.playback

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.player_test.domain.models.Song
import com.example.player_test.playback.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class PlayerManager(
    private val context: Context
) {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _playbackQueue = MutableStateFlow<List<Song>>(emptyList())
    val playbackQueue: StateFlow<List<Song>> = _playbackQueue.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()
    
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()
    
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()
    
    var exoPlayer: ExoPlayer? = null
        private set
    
    init {
        initializePlayer()
        startPlaybackService()
    }
    
    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
                
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.let {
                        updateCurrentSongFromMediaItem(it)
                    }
                }
                
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            _duration.value = duration
                        }
                        Player.STATE_ENDED -> {
                            handlePlaybackEnded()
                        }
                    }
                }
            })
        }
    }
    
    private fun startPlaybackService() {
        val intent = Intent(context, PlaybackService::class.java)
        context.startForegroundService(intent)
    }
    
    fun playPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }
    
    fun play(songs: List<Song>, startIndex: Int = 0) {
        if (songs.isEmpty()) return
        
        _playbackQueue.value = songs
        _currentIndex.value = startIndex
        
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setUri(song.contentUri)
                .setMediaId(song.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .build()
                )
                .build()
        }
        
        exoPlayer?.apply {
            setMediaItems(mediaItems, startIndex, 0L)
            prepare()
            play()
        }
        
        _currentSong.value = songs.getOrNull(startIndex)
    }
    
    fun playNext() {
        exoPlayer?.let { player ->
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem()
                _currentIndex.value = player.currentMediaItemIndex
                updateCurrentSong()
            } else if (_repeatMode.value == RepeatMode.ALL) {
                player.seekTo(0, 0L)
                _currentIndex.value = 0
                updateCurrentSong()
            }
        }
    }
    
    fun playPrevious() {
        exoPlayer?.let { player ->
            if (player.hasPreviousMediaItem()) {
                player.seekToPreviousMediaItem()
                _currentIndex.value = player.currentMediaItemIndex
                updateCurrentSong()
            } else if (_repeatMode.value == RepeatMode.ALL) {
                val lastIndex = _playbackQueue.value.size - 1
                player.seekTo(lastIndex, 0L)
                _currentIndex.value = lastIndex
                updateCurrentSong()
            }
        }
    }
    
    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        _currentPosition.value = position
    }
    
    fun toggleShuffle() {
        val newShuffleState = !_shuffleEnabled.value
        _shuffleEnabled.value = newShuffleState
        exoPlayer?.shuffleModeEnabled = newShuffleState
    }
    
    fun toggleRepeatMode() {
        val newRepeatMode = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.OFF
        }
        _repeatMode.value = newRepeatMode
        
        exoPlayer?.repeatMode = when (newRepeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }
    
    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }
    
    fun updateProgress() {
        exoPlayer?.let { player ->
            _currentPosition.value = player.currentPosition
            _duration.value = player.duration.takeIf { it > 0 } ?: 0L
        }
    }
    
    fun setPlaybackSpeed(speed: Float) {
        val constrainedSpeed = speed.coerceIn(0.25f, 3.0f)
        _playbackSpeed.value = constrainedSpeed
        exoPlayer?.setPlaybackSpeed(constrainedSpeed)
    }
    
    fun increaseSpeed() {
        val currentSpeed = _playbackSpeed.value
        val newSpeed = when {
            currentSpeed < 1.0f -> 1.0f
            currentSpeed < 1.25f -> 1.25f
            currentSpeed < 1.5f -> 1.5f
            currentSpeed < 2.0f -> 2.0f
            else -> 3.0f
        }
        setPlaybackSpeed(newSpeed)
    }
    
    fun decreaseSpeed() {
        val currentSpeed = _playbackSpeed.value
        val newSpeed = when {
            currentSpeed > 2.0f -> 2.0f
            currentSpeed > 1.5f -> 1.5f
            currentSpeed > 1.25f -> 1.25f
            currentSpeed > 1.0f -> 1.0f
            else -> 0.25f
        }
        setPlaybackSpeed(newSpeed)
    }
    
    private fun updateCurrentSongFromMediaItem(mediaItem: MediaItem) {
        val songId = mediaItem.mediaId?.toLongOrNull()
        if (songId != null) {
            val song = _playbackQueue.value.find { it.id == songId }
            _currentSong.value = song
            _currentIndex.value = _playbackQueue.value.indexOf(song).takeIf { it >= 0 } ?: 0
        }
    }
    
    private fun updateCurrentSong() {
        val currentQueue = _playbackQueue.value
        val currentIdx = _currentIndex.value
        _currentSong.value = currentQueue.getOrNull(currentIdx)
    }
    
    private fun handlePlaybackEnded() {
        when (_repeatMode.value) {
            RepeatMode.ONE -> {
                exoPlayer?.seekTo(0L)
                exoPlayer?.play()
            }
            RepeatMode.ALL -> {
                playNext()
            }
            RepeatMode.OFF -> {
                // Stop playback
            }
        }
    }
    
    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}

enum class RepeatMode {
    OFF, ONE, ALL
}