package com.example.player_test.domain.usecase.playlist

import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistSongsUseCase(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(playlistId: Long): Flow<List<Song>> {
        return playlistRepository.getPlaylistSongs(playlistId)
    }
}