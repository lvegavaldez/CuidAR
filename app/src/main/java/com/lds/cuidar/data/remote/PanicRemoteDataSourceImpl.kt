package com.lds.cuidar.data.remote

import retrofit2.Response

class PanicRemoteDataSourceImpl(
    private val panicApi: PanicApi
) : PanicRemoteDataSource {

    override suspend fun sendPanic(request: PanicRequest): Response<Unit> =
        panicApi.sendPanic(request)
}
