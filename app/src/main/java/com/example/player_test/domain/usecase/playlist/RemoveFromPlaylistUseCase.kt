package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.repository.PlaylistRepository

class RemoveFromPlaylistUseCase(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long, songId: Long) {
        playlistRepository.removeSongFromPlaylist(playlistId, songId)
    }
}