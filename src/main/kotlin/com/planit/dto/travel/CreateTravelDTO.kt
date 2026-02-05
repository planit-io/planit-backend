package com.planit.dto.travel

import java.time.LocalDate

data class CreateTravelDTO (
    val destination: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String?,
    val days: Int,
)
