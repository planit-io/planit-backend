package com.planit.service

import com.planit.dto.activity.ActivityDTO
import com.planit.dto.travelAddress.TravelAddressDTO
import com.planit.model.entity.Activity
import com.planit.model.entity.TravelAddress
import com.planit.repository.ActivityRepository
import com.planit.repository.TravelRepository
import com.planit.validator.ActivityValidator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException


@ApplicationScoped
@Transactional
class ActivityService {

    val travelRepository: TravelRepository = TravelRepository()
    val activityRepository = ActivityRepository()


    fun createActivity(travelId: Long, activityDTO: ActivityDTO): ActivityDTO {
        val activityValidator = ActivityValidator()

        val travelEntity = travelRepository.findById(travelId)
            ?: throw IllegalArgumentException("Travel not found")

        activityValidator.validateActivityData(activityDTO)
        val activity = Activity().apply {
            name = activityDTO.name
            description = activityDTO.description ?: ""
            travel = travelEntity
        }
        activity.persist()
        if (activityDTO.travelAddress != null) {
            val travelAddressDTO = activityDTO.travelAddress
            val travelAddress = TravelAddress()
                .apply {
                    address = travelAddressDTO.address
                    note = travelAddressDTO.note ?: ""
                    this.activity = activity
                }
            activity.travelAddress = travelAddress
            activity.persist()
        }

        return ActivityDTO(
            id = activity.id!!,
            name = activity.name,
            description = activity.description,
            completed = activity.completed,
            travelId = travelId,
            travelDayId = null,
            time = null,
            createDate = activity.createdDate.toEpochMilli(),
            lastUpdateDate = activity.lastUpdateDate.toEpochMilli(),
            travelAddress = activity.travelAddress?.let { travelAddress ->
                TravelAddressDTO(
                    id = travelAddress.id!!,
                    address = travelAddress.address,
                    note = travelAddress.note,
                    createDate = travelAddress.createdDate.toEpochMilli(),
                    lastUpdateDate = travelAddress.lastUpdateDate.toEpochMilli(),
                    activityId = activity.id,
                    activityDayId = null
                )
            }
        )


    }

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
                createDate = activity.createdDate.toEpochMilli(),
                lastUpdateDate = activity.lastUpdateDate.toEpochMilli(),
                travelAddress = activity.travelAddress?.let { travelAddress ->
                    TravelAddressDTO(
                        id = travelAddress.id!!,
                        address = travelAddress.address,
                        note = travelAddress.note,
                        createDate = travelAddress.createdDate.toEpochMilli(),
                        lastUpdateDate = travelAddress.lastUpdateDate.toEpochMilli(),
                        activityId = activity.id,
                        activityDayId = null
                    )
                }
            )
        }
    }

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
            activityDTO.travelAddress?.let { travelAddressDTO ->
                if (travelAddress == null) {
                    val newTravelAddress = TravelAddress().apply {
                        address = travelAddressDTO.address
                        note = travelAddressDTO.note ?: ""
                        this.activity = activity
                    }
                    this.travelAddress = newTravelAddress
                } else {
                    travelAddress?.apply {
                        address = travelAddressDTO.address
                        note = travelAddressDTO.note ?: ""
                    }
                }
            }
        }
        activity.persist()

    }

    fun deleteActivity(travelId: Long, activityId: Long) {
        val activity = activityRepository.find(
            "travel.id = ?1 and id = ?2",
            travelId, activityId
        ).firstResult() ?: throw NotFoundException("Activity not found")

        activity.delete()
    }

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
            createDate = activity.createdDate.toEpochMilli(),
            lastUpdateDate = activity.lastUpdateDate.toEpochMilli(),
            travelAddress = activity.travelAddress?.let { travelAddress ->
                TravelAddressDTO(
                    id = travelAddress.id!!,
                    address = travelAddress.address,
                    note = travelAddress.note,
                    createDate = travelAddress.createdDate.toEpochMilli(),
                    lastUpdateDate = travelAddress.lastUpdateDate.toEpochMilli(),
                    activityId = activity.id,
                    activityDayId = null
                )
            }
        )
    }


}