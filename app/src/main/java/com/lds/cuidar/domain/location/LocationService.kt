package com.lds.cuidar.domain.location

interface LocationService {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}
