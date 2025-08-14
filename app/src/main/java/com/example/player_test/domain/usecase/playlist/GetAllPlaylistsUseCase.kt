package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.models.Playlist
import com.example.player_test.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
class GetAllPlaylistsUseCase(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }
}