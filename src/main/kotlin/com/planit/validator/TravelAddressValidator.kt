package com.planit.validator

import com.planit.dto.travelAddress.TravelAddressDTO

class TravelAddressValidator {
    fun validateTravelAddressDTO(travelAddressDTO: TravelAddressDTO) {
        if (travelAddressDTO.address.isBlank()) {
            throw IllegalArgumentException("Travel address cannot be blank")
        }
        if (travelAddressDTO.activityDayId != null &&
            travelAddressDTO.activityId != null) {
            throw IllegalArgumentException("Travel address cannot be associated with both an activity and an activity day")
        }
    }
}