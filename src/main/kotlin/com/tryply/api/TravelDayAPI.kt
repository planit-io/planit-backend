package com.tryply.api

import com.tryply.dto.ActivityDTO
import com.tryply.dto.TravelDTO
import com.tryply.dto.TravelDayDTO
import com.tryply.model.entity.TravelDayEntity
import com.tryply.validator.TravelDayValidator
import com.tryply.repository.TravelDayRepository
import com.tryply.repository.TravelRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.ResponseStatus

@ApplicationScoped
@Path("/travels/{travelId}/travelDays")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class TravelDayAPI {
    val travelRepository : TravelRepository = TravelRepository()
    val travelDayRepository: TravelDayRepository = TravelDayRepository()


    @POST
    fun createTravelDay(
        @PathParam("travelId") travelId: Long,
        travelDayDTO: TravelDayDTO
    ): TravelDTO {

        val travelDayValidator = TravelDayValidator()
        if (!travelDayValidator.validateTravelDayData(travelDayDTO)) {
            throw IllegalArgumentException("Invalid travel day data")
        }

        val travel = travelRepository.find("id", travelId).firstResult()
            ?: throw NotFoundException("Travel not found")

        travel.days += 1

        val travelDayEntity = TravelDayEntity()
        travelDayEntity.dayNumber = travel.days + 1
        travelDayEntity.name = travelDayDTO.name!!
        travelDayEntity.description = travelDayDTO.description!!
        travelDayEntity.travel = travel
        travel.travelDayEntityList.add(travelDayEntity)


        travel.persistAndFlush()

        return TravelDTO(
            id = travel.id,
            destination = travel.destination,
            name = travel.name,
            startDate = travel.startDate,
            code = travel.code,
            endDate = travel.endDate,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayEntityList.map { td -> TravelDayDTO(
                id = td.id,
                dayNumber = td.dayNumber,
                name = td.name,
                description = td.description,
                travelId = travel.id!!,
                activities = null
            ) }
        )
    }

    @PUT
    @Path("/{travelDayId}")
    @ResponseStatus(204)
    fun updateTravelDay(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        travelDayDTO: TravelDayDTO
    ) {
        val travelDayEntity = travelDayRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, travelDayId
        ).firstResult() ?: throw NotFoundException("Travel day not found")

        val travelDayValidator = TravelDayValidator()
        if (!travelDayValidator.validateTravelDayData(travelDayDTO)) {
            throw IllegalArgumentException("Invalid travel day data")
        }

        travelDayEntity.apply {
            name = travelDayDTO.name!!
            description = travelDayDTO.description!!
        }

        travelDayEntity.persist()
    }

    @GET
    fun getTravelDays(@PathParam("travelId") travelId: Long): List<TravelDayDTO> {
        travelDayRepository.find(query="travel.id", Sort.by("dayNumber", Sort.Direction.Ascending), travelId).list().let { travelDayEntities ->
            return travelDayEntities.map { travelDayEntity ->
                TravelDayDTO(
                    id = travelDayEntity.id,
                    dayNumber = travelDayEntity.dayNumber,
                    name = travelDayEntity.name,
                    description = travelDayEntity.description,
                    travelId = travelId,
                    activities = null
                )
            }
        }
    }

    @GET
    @Path("/{travelDayId}")
    fun getTravelDayByNumber(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long
    ): TravelDayDTO {
        val travelDayEntity = travelDayRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, travelDayId
        ).firstResult() ?: throw NotFoundException("Travel day not found")

        return TravelDayDTO(
            id = travelDayEntity.id,
            dayNumber = travelDayEntity.dayNumber,
            name = travelDayEntity.name,
            description = travelDayEntity.description,
            travelId = travelId,
            activities = travelDayEntity.activityDayEntityList.map
            { activityEntity ->
                ActivityDTO(
                    id = activityEntity.id,
                    name = activityEntity.name,
                    description = activityEntity.description,
                    travelId = travelId,
                    travelDayId = travelDayEntity.id!!,
                    time = activityEntity.time
                )
            })
    }

}