package com.lds.cuidar.data.repository

interface PanicRepository {
    suspend fun triggerPanic(lat: Double, lng: Double): Boolean
}
