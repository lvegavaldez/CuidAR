package com.lds.cuidar.data.remote.dto

data class PanicRequestDto(
    val user_id: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
