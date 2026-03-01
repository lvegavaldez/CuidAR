package com.lds.cuidar.domain.model

data class PanicEvent(
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
