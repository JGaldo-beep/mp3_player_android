package com.example.player_test.domain.usecase

import com.example.player_test.domain.models.Song
import com.example.player_test.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
class GetAllSongsUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<Song>> {
        return musicRepository.getAllSongs()
    }
}