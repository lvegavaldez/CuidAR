package com.lds.cuidar.data.remote

import retrofit2.Response

interface PanicRemoteDataSource {
    suspend fun sendPanic(request: PanicRequest): Response<Unit>
}
