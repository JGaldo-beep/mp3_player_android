package com.example.player_test.domain.usecase

import com.example.player_test.domain.models.Album
import com.example.player_test.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
class GetAlbumsUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<Album>> {
        return musicRepository.getAllAlbums()
    }
}