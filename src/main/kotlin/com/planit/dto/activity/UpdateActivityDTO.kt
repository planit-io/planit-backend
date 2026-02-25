package com.planit.dto.activity

import com.planit.dto.travelAddress.TravelAddressDTO

data class UpdateActivityDTO(
    val name: String,
    val description: String?,
    val time: String?,
    val travelAddress: TravelAddressDTO?
)
