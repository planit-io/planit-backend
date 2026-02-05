package com.planit.validator

import com.planit.dto.travel.TravelDTO
import java.time.LocalDate

class TravelValidator {

    fun isValidNumberOfDays(startDate: LocalDate, endDate: LocalDate): Boolean {
        return !endDate.isBefore(startDate)
    }

    fun isValidImageUrl(url: String?): Boolean {
        if (url == null || url.isEmpty()) return true
        val regex = "^(http|https)://.*\\.(jpg|jpeg|png|gif)$".toRegex(RegexOption.IGNORE_CASE)
        return regex.matches(url)
    }

    fun isValidTravelName(name: String): Boolean {
        return name.length in 1..100
    }

    fun isValidDestination(destination: String): Boolean {
        return destination.length in 1..100
    }

    fun validateTravelData(travelDTO : TravelDTO): Boolean {
        return isValidTravelName(travelDTO.name) &&
               isValidDestination(travelDTO.destination) &&
               isValidNumberOfDays(travelDTO.startDate, travelDTO.endDate) &&
               isValidImageUrl(travelDTO.imageUrl)
    }

}