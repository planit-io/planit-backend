package com.tryply.model.response

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String? = null
)