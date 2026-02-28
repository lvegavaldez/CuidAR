package com.lds.cuidar.data.repository

import com.lds.cuidar.data.remote.PanicRemoteDataSource
import com.lds.cuidar.data.remote.PanicRequest

class PanicRepositoryImpl(
    private val remoteDataSource: PanicRemoteDataSource
) : PanicRepository {

    override suspend fun triggerPanic(
        lat: Double,
        lng: Double
    ): Boolean {
        val request = PanicRequest(
            user_id = "test-user",
            lat = lat,
            lng = lng,
            timestamp = System.currentTimeMillis()
        )

        return remoteDataSource.sendPanic(request)
    }
}
