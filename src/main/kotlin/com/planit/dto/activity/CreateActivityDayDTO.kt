package com.planit.dto.activity

import com.planit.dto.travelAddress.TravelAddressDTO

data class CreateActivityDayDTO (
    val name: String,
    val description: String?,
    val time: String?,
    val travelAddress: TravelAddressDTO?
)