package com.planit.dto.travel

import com.planit.model.entity.User
import jakarta.ws.rs.QueryParam
import java.time.LocalDate

class RetrieveTravelDTO {

    @QueryParam("destination")
    val destination: String? = null

    @QueryParam("name")
    val name: String? = null

    @QueryParam("startDate")
    val startDate: LocalDate? = null

    @QueryParam("endDate")
    val endDate: LocalDate? = null

    @QueryParam("userKeycloakId")
    var userKeycloakId: String? = null

}