package com.tryply.config
import org.eclipse.microprofile.openapi.OASFilter
import org.eclipse.microprofile.openapi.models.OpenAPI
import io.smallrye.openapi.internal.models.security.SecurityRequirement

class GlobalSecurityFilter : OASFilter {
    override fun filterOpenAPI(openAPI: OpenAPI) {
        val requirement = SecurityRequirement()
        requirement.addScheme("JWT")

        openAPI.addSecurityRequirement(requirement)
    }
}
