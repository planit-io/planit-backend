package com.planit.resource

import com.planit.dto.activity.ActivityDTO
import com.planit.dto.activity.CreateActivityDTO
import com.planit.dto.activity.UpdateActivityDTO
import com.planit.mapper.ActivityMapper
import com.planit.service.ActivityService
import com.planit.validator.TravelAddressValidator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/api/travels/{travelId}/activities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ActivityResource {


    val activityService : ActivityService
    val activityMapper : ActivityMapper

    @Inject
    constructor(activityService: ActivityService, activityMapper: ActivityMapper) {
        this.activityService = activityService
        this.activityMapper = activityMapper
    }

    @POST
    fun createActivity(travelId: Long, activityDTO: CreateActivityDTO): ActivityDTO {
        val activity = activityMapper.toDTO(activityDTO)
        return activityService.createActivity(travelId, activity)
    }

    @GET
    fun retrieveActivities(travelId: Long): List<ActivityDTO> {
        return activityService.retrieveActivities(travelId)
    }

    @PUT
    @Path("/{activityId}")
    fun updateActivity(travelId: Long, activityId: Long, activityDTO: UpdateActivityDTO) {
        val activity = activityMapper.toDTO(activityDTO)
        activityService.updateActivity(travelId, activityId, activity)
    }

    @DELETE
    @Path("/{activityId}")
    fun deleteActivity(travelId: Long, activityId: Long) {
        activityService.deleteActivity(travelId, activityId)
    }

    @PATCH
    @Path("/{activityId}")
    fun markActivityCompleted(travelId: Long, activityId: Long, completed: Boolean): ActivityDTO {
        return activityService.markActivityCompleted(travelId, activityId, completed)
    }

}
