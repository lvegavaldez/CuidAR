package com.lds.cuidar.data.remote

class PanicRemoteDataSourceImpl(
    private val api: PanicApi
) : PanicRemoteDataSource {

    override suspend fun sendPanic(request: PanicRequest): Boolean {
        return try {
            val response = api.sendPanic(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
