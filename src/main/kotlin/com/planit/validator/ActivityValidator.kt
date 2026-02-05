package com.planit.validator

import com.planit.dto.activity.ActivityDTO

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

    fun validateTime(time: Int?) {
        if (time == null) {
            throw IllegalArgumentException("Time must be in HH:mm format")
        }
    }

    fun validateActivityDayData(activityDTO: ActivityDTO) {
        validateName(activityDTO.name)
        validateDescription(activityDTO.description)
        validateTime(activityDTO.time)
    }

    fun validateActivityData(activityDTO: ActivityDTO) {
        validateName(activityDTO.name)
        validateDescription(activityDTO.description)
    }
}