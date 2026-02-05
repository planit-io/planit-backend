package com.planit.dto.travelday

data class UpdateTravelDayDTO(
    val destination: String,
    val name: String,
    val imageUrl: String?,
    val days: Int,
)
