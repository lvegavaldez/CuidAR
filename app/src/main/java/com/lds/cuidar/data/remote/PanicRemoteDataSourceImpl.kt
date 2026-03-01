package com.lds.cuidar.data.remote

import android.util.Log

class PanicRemoteDataSourceImpl(
    private val api: PanicApi
) : PanicRemoteDataSource {

    override suspend fun sendPanic(request: PanicRequest): Boolean {
        return try {
            Log.d("PANIC_FLOW", "Sending to API")
            val response = api.sendPanic(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
