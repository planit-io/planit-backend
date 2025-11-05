package com.tryply.model.dto

data class TravelDayDTO (
    val id: Long?,
    val travelId: Long,
    val date: String,
    val title: String?,
    val description: String?
)