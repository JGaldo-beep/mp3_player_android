package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.repository.PlaylistRepository

class UpdatePlaylistUseCase(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long, newName: String) {
        playlistRepository.updatePlaylistName(playlistId, newName)
    }
}