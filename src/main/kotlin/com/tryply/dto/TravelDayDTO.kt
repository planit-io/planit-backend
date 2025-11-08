package com.tryply.dto

import java.io.Serializable

data class TravelDayDTO (
    val id: Long?,
    val travelId: Long,
    val dayNumber: Int,
    val name: String?,
    val description: String?,
    val activities: List<ActivityDTO>?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
) : Serializable