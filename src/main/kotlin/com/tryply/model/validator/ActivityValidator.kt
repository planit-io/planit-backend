package com.tryply.model.validator

import com.tryply.model.dto.ActivityDTO

class ActivityValidator {

    fun validateName(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Activity name cannot be blank")
        }
        if (name.length > 100) {
            throw IllegalArgumentException("Activity name cannot exceed 100 characters")
        }
    }

    fun validateDescription(description: String?) {
        if (description != null && description.length > 500) {
            throw IllegalArgumentException("Activity description cannot exceed 500 characters")
        }
    }

    fun validateTime(time: String?) {
        if (time == null) {
            throw IllegalArgumentException("Time must be in HH:mm format")
        }
    }

    fun validateActivityDayData(name: String, description: String?, time: String?) {
        validateName(name)
        validateDescription(description)
        validateTime(time)
    }

    fun validateActivityData(activityDTO: ActivityDTO) {
        validateName(activityDTO.name)
        validateDescription(activityDTO.description)
    }
}