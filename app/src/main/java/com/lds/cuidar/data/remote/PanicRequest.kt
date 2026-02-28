package com.lds.cuidar.data.remote

data class PanicRequest(
    val user_id: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
