package com.tryply.api

import com.tryply.dto.ActivityDTO
import com.tryply.model.entity.ActivityDayEntity
import com.tryply.repository.ActivityDayRepository
import com.tryply.repository.TravelDayRepository
import com.tryply.validator.ActivityValidator
import com.tryply.repository.TravelRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/travels/{travelId}/travelDays/{travelDay}/activities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ActivityDayAPI {

    val travelRepository: TravelRepository = TravelRepository()
    val travelDayRepository: TravelDayRepository = TravelDayRepository()
    val activityDayRepository = ActivityDayRepository()


    @POST
    fun createActivityDay(travelId: Long, travelDayId: Long, activityDTO: ActivityDTO): ActivityDTO {
        val activityValidator = ActivityValidator()

        val travelEntity = travelRepository.findById(travelId)
            ?: throw NotFoundException("Travel not found")

        val travelDayEntity = travelDayRepository.findById(travelDayId)
            ?: throw NotFoundException("Travel Day not found")

        activityValidator.validateActivityDayData(activityDTO)
        val activity = ActivityDayEntity().apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            time = activityDTO.time ?: ""
            completed = false
            travelDay = travelDayEntity
        }
        activity.persistAndFlush()

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = travelEntity.id!!,
            travelDayId = travelDayEntity.id!!,
            time = activity.time,
            createDate = activity.createdDate,
            lastUpdateDate = activity.lastUpdateDate
        )


    }

    @GET
    fun retrieveActivities(travelId: Long, travelDayId: Long): List<ActivityDTO> {
        val activities = activityDayRepository.find("travelDay.id = ?1 and travelDay.travel.id = ?2", travelDayId, travelId).list()

        return activities.map { activity ->
            ActivityDTO(
                id = activity.id!!,
                name = activity.name,
                description = activity.description,
                completed = activity.completed,
                travelId = travelId,
                travelDayId = travelDayId,
                time = activity.time,
                createDate = activity.createdDate,
                lastUpdateDate = activity.lastUpdateDate
            )
        }
    }

    @PUT
    @Path("/{activityId}")
    fun updateActivity(travelId: Long, travelDayId: Long, activityId: Long, activityDTO: ActivityDTO) {
        val activityValidator = ActivityValidator()
        val activity = activityDayRepository.find(
            "travelDay.travel.id = ?1 and travelDay.id = ?2 and id = ?3",
            travelId, travelDayId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")
        activityValidator.validateActivityData(activityDTO)
        activity.apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            time = activityDTO.time ?: ""
        }
        activity.persist()

    }

    @DELETE
    @Path("/{activityId}")
    fun deleteActivity(travelId: Long, travelDayId: Long, activityId: Long) {
        val activity = activityDayRepository.find(
            "travelDay.travel.id = ?1 and travelDay.id = ?2 and id = ?3",
            travelId, travelDayId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.delete()
    }

    @PATCH
    @Path("/{activityId}")
    fun markActivityCompleted(travelId: Long, travelDayId: Long, activityId: Long, completed: Boolean): ActivityDTO {
        val activity = activityDayRepository.find(
            "travelDay.travel.id = ?1 and travelDay.id = ?2 and id = ?3",
            travelId, travelDayId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.completed = completed
        activity.persist()

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = activity.travelDay?.travel?.id,
            travelDayId = activity.travelDay?.id,
            time = activity.time,
            createDate = activity.createdDate,
            lastUpdateDate = activity.lastUpdateDate
        )
    }




}
