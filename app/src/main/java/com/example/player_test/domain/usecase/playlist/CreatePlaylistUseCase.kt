package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.repository.PlaylistRepository
class CreatePlaylistUseCase(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(name: String): Long {
        return playlistRepository.createPlaylist(name)
    }
}