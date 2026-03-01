package com.lds.cuidar.domain.usecase

import com.lds.cuidar.domain.model.PanicEvent
import com.lds.cuidar.domain.repository.PanicRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class TriggerPanicUseCaseTest {

    @Test
    fun `should call repository when triggered`() = runTest {
        val fakeRepo = FakePanicRepository()
        val useCase = TriggerPanicUseCase(fakeRepo)

        useCase(
            PanicEvent(
                userId = "user",
                latitude = 1.0,
                longitude = 1.0,
                timestamp = 123L
            )
        )

        assertTrue(fakeRepo.called)
    }
}

private class FakePanicRepository : PanicRepository {
    var called = false

    override suspend fun send(event: PanicEvent): Result<Unit> {
        called = true
        return Result.success(Unit)
    }
}
