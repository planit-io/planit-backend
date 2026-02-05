package com.planit.dto.activity

data class ActivityDTO(
    val id: Long?,
    val travelId: Long?,
    val travelDayId: Long?,
    val name: String,
    val description: String?,
    val time: Int?,
    val completed: Boolean = false,
    val createDate: Long?,
    val lastUpdateDate: Long?,
)