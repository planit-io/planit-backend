package com.planit.dto.activity

data class RetrieveActivityDTO(
    val travelId: Long?,
    val travelDayId: Long?,
    val name: String,
    val completed : Boolean?,
)