package com.tryply.api

import com.tryply.dto.ActivityDTO
import com.tryply.model.entity.ActivityEntity
import com.tryply.validator.ActivityValidator
import com.tryply.repository.ActivityRepository
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
@Path("/travel/{travelId}/activities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ActivityAPI {

    val travelRepository: TravelRepository = TravelRepository()
    val activityRepository = ActivityRepository()


    @POST
    fun createActivity(travelId: Long, activityDTO: ActivityDTO): ActivityDTO {
        val activityValidator = ActivityValidator()

        val travelEntity = travelRepository.findById(travelId)
            ?: throw IllegalArgumentException("Travel not found")

        activityValidator.validateActivityData(activityDTO)
        val activity = ActivityEntity().apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            travel = travelEntity
        }
        activity.persistAndFlush()

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = travelId,
            travelDayId = null,
            time = null,
            createDate = activity.createdDate,
            lastUpdateDate = activity.lastUpdateDate
        )


    }

    @GET
    fun retrieveActivities(travelId: Long): List<ActivityDTO> {
        val activities = activityRepository.find("travel.id", travelId).list()

        return activities.map { activity ->
            ActivityDTO(
                id = activity.id!!,
                name = activity.name,
                description = activity.description,
                completed = activity.completed,
                travelId = travelId,
                travelDayId = null,
                time = null,
                createDate = activity.createdDate,
                lastUpdateDate = activity.lastUpdateDate
            )
        }
    }

    @PUT
    @Path("/{activityId}")
    fun updateActivity(travelId: Long, activityId: Long, activityDTO: ActivityDTO) {
        val activityValidator = ActivityValidator()
        val activity = activityRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")
        activityValidator.validateActivityData(activityDTO)
        activity.apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
        }
        activity.persist()

    }

    @DELETE
    @Path("/{activityId}")
    fun deleteActivity(travelId: Long, activityId: Long) {
        val activity = activityRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.delete()
    }

    @PATCH
    @Path("/{activityId}")
    fun markActivityCompleted(travelId: Long, activityId: Long, completed: Boolean): ActivityDTO {
        val activity = activityRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.completed = completed
        activity.persist()

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = travelId,
            travelDayId = null,
            time = null,
            createDate = activity.createdDate,
            lastUpdateDate = activity.lastUpdateDate
        )
    }




}
