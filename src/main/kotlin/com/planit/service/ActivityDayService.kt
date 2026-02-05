package com.planit.service

import com.planit.dto.activity.ActivityDTO
import com.planit.model.entity.ActivityDay
import com.planit.repository.ActivityDayRepository
import com.planit.repository.TravelDayRepository
import com.planit.repository.TravelRepository
import com.planit.validator.ActivityValidator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException

@ApplicationScoped
@Transactional
class ActivityDayService {

    val travelRepository: TravelRepository = TravelRepository()
    val travelDayRepository: TravelDayRepository = TravelDayRepository()
    val activityDayRepository = ActivityDayRepository()

    fun createActivityDay(
        travelId: Long,
        travelDayId: Long,
        activityDTO: ActivityDTO
    ): ActivityDTO {
        val activityValidator = ActivityValidator()

        val travelEntity = travelRepository.findById(travelId)
            ?: throw NotFoundException("Travel not found")

        val travelDayEntity = travelDayRepository.findById(travelDayId)
            ?: throw NotFoundException("Travel Day not found")

        activityValidator.validateActivityDayData(activityDTO)
        val activity = ActivityDay().apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            time = activityDTO.time
            completed = false
            travelDay = travelDayEntity
        }
        activity.persist()

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = travelEntity.id!!,
            travelDayId = travelDayEntity.id!!,
            time = activity.time,
            createDate = activity.createdDate.toEpochMilli(),
            lastUpdateDate = activity.lastUpdateDate.toEpochMilli()
        )


    }

    fun retrieveActivities(
        travelId: Long,
        travelDayId: Long
    ): List<ActivityDTO> {
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
                createDate = activity.createdDate.toEpochMilli(),
                lastUpdateDate = activity.lastUpdateDate.toEpochMilli()
            )
        }
    }

    fun updateActivity(
        travelId: Long,
        travelDayId: Long,
        activityId: Long,
        activityDTO: ActivityDTO
    ) {
        val activityValidator = ActivityValidator()
        val activity = activityDayRepository.find(
            "travelDay.travel.id = ?1 and travelDay.id = ?2 and id = ?3",
            travelId, travelDayId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")
        activityValidator.validateActivityData(activityDTO)
        activity.apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            time = activityDTO.time
        }
        activity.persist()

    }

    fun deleteActivity(
        travelId: Long,
        travelDayId: Long,
        activityId: Long
    ) {
        val activity = activityDayRepository.find(
            "travelDay.travel.id = ?1 and travelDay.id = ?2 and id = ?3",
            travelId, travelDayId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.delete()
    }

    fun markActivityCompleted(
        travelId: Long,
        travelDayId: Long,
        activityId: Long,
        completed: Boolean
    ): ActivityDTO {
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
            createDate = activity.createdDate.toEpochMilli(),
            lastUpdateDate = activity.lastUpdateDate.toEpochMilli()
        )
    }

}