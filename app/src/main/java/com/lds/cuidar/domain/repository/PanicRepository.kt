package com.lds.cuidar.domain.repository

import com.lds.cuidar.domain.model.PanicEvent

interface PanicRepository {
    suspend fun send(event: PanicEvent): Result<Unit>
}
