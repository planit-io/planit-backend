package com.tryply.model.dto


data class TravelDTO(
    val id: Long?,
    val destination: String,
    val name: String,
    val code: String?,
    val startDate: String,
    val endDate: String
)