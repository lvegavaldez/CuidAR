package com.lds.cuidar.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PanicApi {
    @POST(".")
    suspend fun sendPanic(
        @Body request: PanicRequest
    ): Response<ResponseBody>
}
