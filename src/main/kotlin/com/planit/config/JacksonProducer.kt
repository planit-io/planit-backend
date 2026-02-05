package com.planit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import java.text.SimpleDateFormat
import java.util.TimeZone

//@ApplicationScoped
//class JacksonProducer {
//
//    @Produces
//    fun objectMapper(): ObjectMapper {
//        val mapper = ObjectMapper()
//        mapper.registerModule(JavaTimeModule())
//        mapper.setDateFormat(SimpleDateFormat("yyyy-MM-dd"))
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//        mapper.setTimeZone(TimeZone.getTimeZone("UTC"))
//        return mapper
//    }
//}