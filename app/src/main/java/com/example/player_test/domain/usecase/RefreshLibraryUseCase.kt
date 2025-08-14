package com.example.player_test.domain.usecase

import com.example.player_test.domain.repository.MusicRepository
class RefreshLibraryUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() {
        musicRepository.refreshLibrary()
    }
}