package com.tryply.dto



data class TravelDTO(
    val id: Long?,
    val destination: String,
    val name: String,
    val code: String?,
    val startDate: String,
    val endDate: String,
    val imageUrl: String?,
    val days: Int,
    val travelDays: List<TravelDayDTO>?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
)