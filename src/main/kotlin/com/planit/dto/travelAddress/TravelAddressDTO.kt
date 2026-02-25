package com.planit.dto.travelAddress

import java.util.Date

data class TravelAddressDTO (
    val id : Long?,
    val address : String,
    val note: String?,
    val activityId: Long?,
    val activityDayId : Long?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
)