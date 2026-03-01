package com.lds.cuidar.data.remote

import com.lds.cuidar.data.remote.dto.PanicRequestDto

interface PanicRemoteDataSource {
    suspend fun sendPanic(request: PanicRequestDto)
}
