package com.planit.resource

import com.planit.dto.activity.ActivityDTO
import com.planit.dto.activity.CreateActivityDTO
import com.planit.dto.activity.CreateActivityDayDTO
import com.planit.dto.activity.UpdateActivityDTO
import com.planit.mapper.ActivityMapper
import com.planit.service.ActivityDayService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/api/travels/{travelId}/travelDays/{travelDayId}/activities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ActivityDayResource {


    val activityDayService : ActivityDayService
    val activityMapper : ActivityMapper

    @Inject
    constructor(activityDayService: ActivityDayService, activityMapper: ActivityMapper) {
        this.activityDayService = activityDayService
        this.activityMapper = activityMapper
    }


    @POST
    fun createActivityDay(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        activityDTO: CreateActivityDayDTO
    ): ActivityDTO {
        val activity = activityMapper.toDTO(activityDTO)
        return activityDayService.createActivityDay(travelId, travelDayId, activity)
    }

    @GET
    fun retrieveActivities(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long
    ): List<ActivityDTO> {
        return activityDayService.retrieveActivities(travelId, travelDayId)
    }

    @PUT
    @Path("/{activityId}")
    fun updateActivity(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        @PathParam("activityId") activityId: Long,
        activityDTO: UpdateActivityDTO
    ) {
        val activity = activityMapper.toDTO(activityDTO)
        activityDayService.updateActivity(travelId, travelDayId, activityId, activity)
    }

    @DELETE
    @Path("/{activityId}")
    fun deleteActivity(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        @PathParam("activityId") activityId: Long
    ) {
        activityDayService.deleteActivity(travelId, travelDayId, activityId)
    }

    @PATCH
    @Path("/{activityId}")
    fun markActivityCompleted(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") travelDayId: Long,
        @PathParam("activityId") activityId: Long,
        completed: Boolean
    ): ActivityDTO {
        return activityDayService.markActivityCompleted(travelId, travelDayId, activityId, completed)
    }

    @PUT
    @Path("/{activityId}/move")
    fun moveActivity(
        @PathParam("travelId") travelId: Long,
        @PathParam("travelDayId") sourceTravelDayId: Long,
        @PathParam("activityId") activityId: Long,
        @QueryParam("targetTravelDayId") targetTravelDayId: Long
    ) {
        activityDayService.moveActivity(travelId, sourceTravelDayId, activityId, targetTravelDayId)
    }

}
