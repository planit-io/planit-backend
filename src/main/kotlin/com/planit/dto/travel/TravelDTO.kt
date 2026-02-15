package com.planit.dto.travel

import com.planit.dto.travelday.TravelDayDTO
import java.time.LocalDate

data class TravelDTO(
    val id: Long?,
    val description: String?,
    val destination: String,
    val name: String,
    val code: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String?,
    val days: Int,
    val travelDays: List<TravelDayDTO>?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
)