package com.raineyi.composition.domain.repository

import com.raineyi.composition.domain.entity.GameSettings
import com.raineyi.composition.domain.entity.Level
import com.raineyi.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}