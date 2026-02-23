package com.planit.validator

import com.planit.dto.travelday.TravelDayDTO

class TravelDayValidator {

    fun isValidTravelDayString(name: String?): Boolean {
        if (name.isNullOrBlank()) {
            return false
        }
        val trimmedName = name.trim()
        return trimmedName.length in 3..100
    }

    fun validateTravelDayDescription(description: String?): Boolean {
        if (description.isNullOrBlank()) {
            return true
        }
        val trimmedDescription = description.trim()
        return trimmedDescription.length <= 500
    }

    fun isValidDayNumber(dayNumber: Int): Boolean {
        return dayNumber > 0
    }

    fun validateTravelDayData(travelDate : TravelDayDTO): Boolean {
        return true
    }

}