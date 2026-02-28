package com.lds.cuidar.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PanicApi {
    @POST("panic")
    suspend fun sendPanic(
        @Body request: PanicRequest
    ): Response<Unit>
}
