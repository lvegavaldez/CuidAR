package com.lds.cuidar.data.repository

import com.lds.cuidar.data.mapper.toDto
import com.lds.cuidar.data.remote.PanicRemoteDataSource
import com.lds.cuidar.domain.model.PanicEvent
import com.lds.cuidar.domain.repository.PanicRepository

class PanicRepositoryImpl(
    private val remoteDataSource: PanicRemoteDataSource
) : PanicRepository {

    override suspend fun send(event: PanicEvent): Result<Unit> {
        return try {
            remoteDataSource.sendPanic(event.toDto())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
