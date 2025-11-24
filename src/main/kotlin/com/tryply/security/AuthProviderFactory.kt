package com.tryply.security

import io.quarkus.arc.DefaultBean
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class AuthProviderFactory(
    @ConfigProperty(name = "auth.provider", defaultValue = "local")
    private val keycloakProvider: KeycloakAuthProvider
) {

    @Produces
    @DefaultBean
    fun produce(): AuthProvider {
        return keycloakProvider
    }
}