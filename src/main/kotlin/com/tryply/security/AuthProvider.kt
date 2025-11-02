package com.tryply.security

import com.tryply.model.auth.AuthUserClaims

interface AuthProvider {
    fun validateToken(token: String): AuthUserClaims
}