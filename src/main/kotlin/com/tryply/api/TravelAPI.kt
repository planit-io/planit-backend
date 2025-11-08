package com.tryply.api

import com.tryply.dto.TravelDTO
import com.tryply.dto.TravelDayDTO
import com.tryply.model.entity.TravelEntity
import com.tryply.validator.TravelValidator
import com.tryply.repository.TravelRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/travels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class TravelAPI {

    val travelRepository = TravelRepository()

    @POST
    fun createTravel(travelDTO: TravelDTO): TravelDTO {

        val travelValidator = TravelValidator()
        if (!travelValidator.validateTravelData(travelDTO)) {
            throw IllegalArgumentException("Invalid travel data")
        }

        val travel = TravelEntity().apply {
            destination = travelDTO.destination
            name = travelDTO.name
            startDate = travelDTO.startDate
            endDate = travelDTO.endDate
            days = travelDTO.days
            imageUrl = travelDTO.imageUrl
        }
        travel.generateCode()
        travel.generateTravelDays()

        travel.persist()

        return TravelDTO(
            id = travel.id,
            destination = travel.destination,
            name = travel.name,
            startDate = travel.startDate,
            code = travel.code,
            endDate = travel.endDate,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayEntityList.map {TravelDayDTO(
                id = it.id,
                dayNumber = it.dayNumber,
                name = it.name,
                description = it.description,
                travelId = travel.id!!,
                activities = null,
                createDate = it.createdDate,
                lastUpdateDate = it.lastUpdateDate
            ) },
            createDate = travel.createdDate,
            lastUpdateDate = travel.lastUpdateDate
        )
    }

    @GET
    fun getAllTravels(): List<TravelDTO> {
        return travelRepository.listAll().map { travel ->
            TravelDTO(
                id = travel.id,
                destination = travel.destination,
                name = travel.name,
                startDate = travel.startDate,
                code = travel.code,
                endDate = travel.endDate,
                imageUrl = travel.imageUrl,
                days = travel.days,
                travelDays = emptyList(),
                createDate = travel.createdDate,
                lastUpdateDate = travel.lastUpdateDate
            )
        }
    }

    @GET
    @Path("/{travelId}")
    fun getTravelById(@PathParam("travelId") travelId: Long): TravelDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel with id $travelId not found")
        return TravelDTO(
            id = travel.id,
            destination = travel.destination,
            name = travel.name,
            code = travel.code,
            startDate = travel.startDate,
            endDate = travel.endDate,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayEntityList.map {TravelDayDTO(
                id = it.id,
                dayNumber = it.dayNumber,
                name = it.name,
                description = it.description,
                travelId = travel.id!!,
                activities = null,
                createDate = it.createdDate,
                lastUpdateDate = it.lastUpdateDate
            ) },
            createDate = travel.createdDate,
            lastUpdateDate = travel.lastUpdateDate
        )
    }
}