package com.tryply.model.dto

data class ActivityDTO(
    val id: Long?,
    val travelId: Long?,
    val travelDayId: Long?,
    val name: String,
    val description: String?,
    val date: String,
    val time: String?
)