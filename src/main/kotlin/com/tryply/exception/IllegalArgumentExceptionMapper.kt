package com.tryply.exception

import com.tryply.model.response.ErrorResponse
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(exception: IllegalArgumentException): Response {
        val body = ErrorResponse(
            status = Response.Status.CONFLICT.statusCode,
            error = "Conflict",
            message = exception.message
        )
        return Response.status(Response.Status.CONFLICT)
            .entity(body)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}