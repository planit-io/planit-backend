package com.planit.dto.travelday

import com.planit.dto.activity.ActivityDTO
import java.time.LocalDate

data class TravelDayDTO (
    val id: Long?,
    val travelId: Long,
    val dayNumber: Int,
    val date: LocalDate?,
    val activities: List<ActivityDTO>?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
)