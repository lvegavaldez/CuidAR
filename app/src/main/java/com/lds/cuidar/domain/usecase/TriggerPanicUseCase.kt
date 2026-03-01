package com.lds.cuidar.domain.usecase

import com.lds.cuidar.domain.model.PanicEvent
import com.lds.cuidar.domain.repository.PanicRepository

class TriggerPanicUseCase(
    private val repository: PanicRepository
) {
    suspend operator fun invoke(event: PanicEvent): Result<Unit> {
        return repository.send(event)
    }
}
