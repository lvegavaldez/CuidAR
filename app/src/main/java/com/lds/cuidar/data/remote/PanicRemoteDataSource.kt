package com.lds.cuidar.data.remote

interface PanicRemoteDataSource {
    suspend fun sendPanic(request: PanicRequest): Boolean
}
