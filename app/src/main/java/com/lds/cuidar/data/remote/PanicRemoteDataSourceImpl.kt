package com.lds.cuidar.data.remote

import android.util.Log
import com.lds.cuidar.data.remote.dto.PanicRequestDto
import java.io.IOException

class PanicRemoteDataSourceImpl(
    private val api: PanicApi
) : PanicRemoteDataSource {

    override suspend fun sendPanic(request: PanicRequestDto) {
        Log.d("PANIC_FLOW", "Sending to API")
        val response = api.sendPanic(request)

        if (!response.isSuccessful) {
            throw IOException("sendPanic failed with HTTP ${response.code()}")
        }
    }
}
