package com.tryply.security

import com.tryply.model.auth.AuthUserClaims
import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

@ApplicationScoped
class LocalAuthProvider @Inject constructor(
    private val jwtParser: JWTParser
) : AuthProvider {

    override fun validateToken(token: String): AuthUserClaims {
        try {
            val jwt = jwtParser.parse(token)
            val userId = jwt.subject ?: throw Exception("No subject")
            val email = jwt.getClaim<String>("email")
            val roles = jwt.getClaim<List<String>>("roles") ?: listOf("user")

            return AuthUserClaims(userId, email, roles)
        } catch (e: Exception) {
            throw WebApplicationException(
                "Invalid token",
                Response.Status.UNAUTHORIZED
            )
        }
    }
}
