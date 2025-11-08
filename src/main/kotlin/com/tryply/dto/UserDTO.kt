package com.tryply.dto

import java.io.Serializable

data class UserDTO (
    val id: Long?,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String?,
    val createDate: Long?,
    val lastUpdateDate: Long?,
) : Serializable