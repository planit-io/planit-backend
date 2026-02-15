package com.planit.dto.travel

import java.time.LocalDate

data class UpdateTravelDTO(
    val description: String,
    val destination: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String?,
    val days: Int,
)
