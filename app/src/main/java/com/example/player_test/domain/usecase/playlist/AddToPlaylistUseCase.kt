package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.repository.PlaylistRepository

class AddToPlaylistUseCase(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long, songIds: List<Long>) {
        playlistRepository.addSongsToPlaylist(playlistId, songIds)
    }
}