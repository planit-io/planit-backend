package com.planit.service

import com.planit.dto.activity.ActivityDTO
import com.planit.dto.travel.TravelDTO
import com.planit.dto.travelday.TravelDayDTO
import com.planit.model.entity.TravelDay
import com.planit.repository.TravelDayRepository
import com.planit.repository.TravelRepository
import com.planit.validator.TravelDayValidator
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import org.hibernate.Hibernate

@ApplicationScoped
@Transactional
class TravelDayService {

    val travelRepository : TravelRepository = TravelRepository()
    val travelDayRepository: TravelDayRepository = TravelDayRepository()


    fun createTravelDay(
        travelId: Long,
        travelDayDTO: TravelDayDTO
    ): TravelDTO {

        val travelDayValidator = TravelDayValidator()
        if (!travelDayValidator.validateTravelDayData(travelDayDTO)) {
            throw IllegalArgumentException("Invalid travel day data")
        }

        val travel = travelRepository.find("id", travelId).firstResult()
            ?: throw NotFoundException("Travel not found")

        travel.days += 1

        val travelDay = TravelDay()
        travelDay.dayNumber = travel.days + 1
        travelDay.name = travelDayDTO.name!!
        travelDay.description = travelDayDTO.description!!
        travelDay.travel = travel
        travel.travelDayList.add(travelDay)


        travel.persist()

        return TravelDTO(
            id = travel.id,
            description = travel.description ?: "",
            destination = travel.destination,
            name = travel.name,
            startDate = travel.startDate!!,
            code = travel.code,
            endDate = travel.endDate!!,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayList.map { td -> TravelDayDTO(
                id = td.id,
                dayNumber = td.dayNumber,
                name = td.name,
                description = td.description,
                travelId = travel.id!!,
                activities = null,
                createDate = td.createdDate.toEpochMilli(),
                lastUpdateDate = td.lastUpdateDate.toEpochMilli()
            ) },
            createDate = travel.createdDate.toEpochMilli(),
            lastUpdateDate = travel.lastUpdateDate.toEpochMilli()
        )
    }

    fun updateTravelDay(
        travelId: Long,
        travelDayId: Long,
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

    fun getTravelDays(travelId: Long): List<TravelDayDTO> {
        travelDayRepository.find(query="travel.id", Sort.by("dayNumber", Sort.Direction.Ascending), travelId).list().let { travelDayEntities ->
            return travelDayEntities.map { travelDayEntity ->
                TravelDayDTO(
                    id = travelDayEntity.id,
                    dayNumber = travelDayEntity.dayNumber,
                    name = travelDayEntity.name,
                    description = travelDayEntity.description,
                    travelId = travelId,
                    activities = travelDayEntity.activityDayList.map { activity ->
                        ActivityDTO(
                            id = activity.id!!,
                            name = activity.name,
                            description = activity.description,
                            completed = activity.completed,
                            travelId = travelId,
                            travelDayId = travelDayEntity.id,
                            time = activity.time,
                            createDate = activity.createdDate.toEpochMilli(),
                            lastUpdateDate = activity.lastUpdateDate.toEpochMilli()
                        )
                    },
                    createDate = travelDayEntity.createdDate.toEpochMilli(),
                    lastUpdateDate = travelDayEntity.lastUpdateDate.toEpochMilli()
                )
            }
        }
    }

    fun getTravelDayByNumber(
        travelId: Long,
        travelDayId: Long
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
            createDate = travelDayEntity.createdDate.toEpochMilli(),
            lastUpdateDate = travelDayEntity.lastUpdateDate.toEpochMilli(),
            activities = travelDayEntity.activityDayList.map
            { activityEntity ->
                ActivityDTO(
                    id = activityEntity.id,
                    name = activityEntity.name,
                    description = activityEntity.description,
                    travelId = travelId,
                    travelDayId = travelDayEntity.id!!,
                    time = activityEntity.time,
                    completed = activityEntity.completed,
                    createDate = activityEntity.createdDate.toEpochMilli(),
                    lastUpdateDate = activityEntity.lastUpdateDate.toEpochMilli()
                )
            })
    }

}