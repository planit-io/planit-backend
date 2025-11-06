package com.tryply.model.dto

import java.io.Serializable

data class ActivityDTO(
    val id: Long?,
    val travelId: Long?,
    val travelDayId: Long?,
    val name: String,
    val description: String?,
    val time: String?,
    val completed: Boolean = false
) : Serializable