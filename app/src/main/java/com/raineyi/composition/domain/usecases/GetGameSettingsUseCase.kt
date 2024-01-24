package com.raineyi.composition.domain.usecases

import com.raineyi.composition.domain.repository.GameRepository
import com.raineyi.composition.domain.entity.GameSettings
import com.raineyi.composition.domain.entity.Level

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}