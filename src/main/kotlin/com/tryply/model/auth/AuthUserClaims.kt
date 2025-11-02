package com.tryply.model.auth

data class AuthUserClaims (
    val userId: String,
    val email: String,
    val roles: List<String>
)