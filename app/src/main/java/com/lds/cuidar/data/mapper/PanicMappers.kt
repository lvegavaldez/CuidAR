package com.lds.cuidar.data.mapper

import com.lds.cuidar.data.remote.dto.PanicRequestDto
import com.lds.cuidar.domain.model.PanicEvent

fun PanicEvent.toDto() = PanicRequestDto(
    user_id = userId,
    lat = latitude,
    lng = longitude,
    timestamp = timestamp
)
