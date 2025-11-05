package com.tryply.exception

import com.tryply.model.response.ErrorResponse
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.jboss.logging.Logger

@Provider
@ApplicationScoped
class GenericExceptionMapper : ExceptionMapper<Throwable> {

    private val log: Logger = Logger.getLogger(GenericExceptionMapper::class.java)

    override fun toResponse(exception: Throwable): Response {
        log.error("Unhandled error", exception)
        val body = ErrorResponse(
            status = Response.Status.INTERNAL_SERVER_ERROR.statusCode,
            error = "Internal Server Error",
            message = "Unexpected error"
        )
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(body)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}