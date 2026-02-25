package com.planit.dto.activity

import com.planit.dto.travelAddress.TravelAddressDTO

class CreateActivityDTO (
    val name: String,
    val description: String?,
    val travelAddress: TravelAddressDTO?
)
