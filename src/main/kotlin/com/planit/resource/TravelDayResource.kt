package com.planit.resource

import com.planit.dto.travel.TravelDTO
import com.planit.dto.travelday.CreateTravelDayDTO
import com.planit.dto.travelday.TravelDayDTO
import com.planit.dto.travelday.UpdateTravelDayDTO
import com.planit.mapper.TravelDayMapper
import com.planit.service.TravelDayService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.ResponseStatus

@ApplicationScoped
@Path("/api/travels/{travelId}/travelDays")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class TravelDayResource {

    val travelDayService: TravelDayService
    val travelDayMapper: TravelDayMapper

    @Inject
    constructor(travelDayService: TravelDayService, travelDayMapper: TravelDayMapper) {
        this.travelDayService = travelDayService
        this.travelDayMapper = travelDayMapper
    }


    @POST
    fun createTravelDay(
        @PathParam("travelId") travelId: Long,
        travelDayDTO: CreateTravelDayDTO,
    ): TravelDTO {
        return travelDayService.createTravelDay(travelId, travelDayDTO.day)
    }


    @PUT
    @Path("/{travelDayId}")
    @ResponseStatus(204)
    fun updateTravelDay(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        travelDayDTO: UpdateTravelDayDTO
    ) {
        val travelDay = travelDayMapper.toDTO(travelDayDTO)
        travelDayService.updateTravelDay(travelId, travelDayId, travelDay)
    }

    @GET
    fun getTravelDays(@PathParam("travelId") travelId: Long): List<TravelDayDTO> {
        return travelDayService.getTravelDays(travelId)
    }

    @GET
    @Path("/{travelDayId}")
    fun getTravelDayByNumber(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long
    ): TravelDayDTO {
        return travelDayService.getTravelDayByNumber(travelId, travelDayId)
    }

}