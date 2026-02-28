package com.lds.cuidar.domain

interface LocationService {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}