package com.planit.security

import com.planit.model.entity.User
import com.planit.repository.UserRepository
import org.eclipse.microprofile.jwt.JsonWebToken
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject

@RequestScoped
class SecurityContext @Inject constructor(
    private val jwt: JsonWebToken,
    private val userRepository: UserRepository
) {

    fun currentUserId(): String? = jwt.subject

    fun <T> claim(name: String): T? = jwt.getClaim(name)

    fun rawToken(): String? = jwt.rawToken

    fun findCurrentUser() : User? = currentUserId()?.let { userRepository.find("keycloakId", it).firstResult()  }
}
