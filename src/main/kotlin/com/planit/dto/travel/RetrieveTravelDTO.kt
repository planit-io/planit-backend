package com.planit.dto.travel

import java.time.LocalDate

data class RetrieveTravelDTO (
    val destination: String?,
    val name: String?,
    val code: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
)