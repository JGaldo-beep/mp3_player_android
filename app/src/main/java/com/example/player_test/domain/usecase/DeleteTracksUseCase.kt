package com.example.player_test.domain.usecase

import com.example.player_test.domain.repository.MusicRepository
class DeleteTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(songIds: List<Long>): Result<Unit> {
        return musicRepository.deleteSongs(songIds)
    }
}