package com.tryply.dto.traveler

import com.tryply.model.enums.TravelerRole

data class TravelerDTO (
    val id: Long,
    val username: String,
    val role : TravelerRole?,
    val createdDate: Long,
    val lastUpdatedDate: Long,
    val travelId: Long
    )