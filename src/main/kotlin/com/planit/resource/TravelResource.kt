package com.planit.resource

import com.planit.dto.travel.CreateTravelDTO
import com.planit.dto.travel.RetrieveTravelDTO
import com.planit.dto.travel.TravelDTO
import com.planit.mapper.TravelMapper
import com.planit.model.entity.Travel
import com.planit.service.TravelService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.apache.http.HttpStatus
import org.jboss.resteasy.reactive.ResponseStatus

@ApplicationScoped
@Path("/api/travels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class TravelResource {

    val travelService : TravelService
    val travelMapper : TravelMapper

    @Inject
    constructor(travelService: TravelService, travelMapper: TravelMapper) {
        this.travelService = travelService
        this.travelMapper = travelMapper
    }

    @POST
    @ResponseStatus(HttpStatus.SC_CREATED)
    fun createTravel(travelDTO: CreateTravelDTO): TravelDTO {
        val travel = travelMapper.toDTO(travelDTO)
        return travelService.createTravel(travel)
    }

    @GET
    fun getAllTravels(@BeanParam retrieveTravelDto: RetrieveTravelDTO): List<TravelDTO> {
        return travelService.getAllTravels(retrieveTravelDto)
    }

    @GET
    @Path("/{travelId}")
    fun getTravelById(@PathParam("travelId") travelId: Long): TravelDTO {
        return travelService.getTravelById(travelId)
    }
}