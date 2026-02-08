package com.planit.service

import com.planit.dto.travel.RetrieveTravelDTO
import com.planit.dto.travel.TravelDTO
import com.planit.dto.travelday.TravelDayDTO
import com.planit.model.entity.Travel
import com.planit.model.entity.Traveler
import com.planit.model.enums.TravelerRole
import com.planit.repository.TravelRepository
import com.planit.security.SecurityContext
import com.planit.validator.TravelValidator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException

@ApplicationScoped
@Transactional
class TravelService (
    private val securityContext: SecurityContext,
    val travelRepository: TravelRepository,
    val travelerRepository: TravelRepository
){



    fun createTravel(travelDTO: TravelDTO): TravelDTO {

        val travelValidator = TravelValidator()
        if (!travelValidator.validateTravelData(travelDTO)) {
            throw IllegalArgumentException("Invalid travel data")
        }

        val travel = Travel().apply {
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

        val user = securityContext.findCurrentUser()
        if (user != null) {
            Traveler().apply {
                this.user = user
                this.travel = travel
                this.role = TravelerRole.ADMIN
            }.persist()
        }

        return TravelDTO(
            id = travel.id,
            destination = travel.destination,
            name = travel.name,
            startDate = travel.startDate!!,
            code = travel.code,
            endDate = travel.endDate!!,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayList.map {TravelDayDTO(
                id = it.id,
                dayNumber = it.dayNumber,
                name = it.name,
                description = it.description,
                travelId = travel.id!!,
                activities = null,
                createDate = it.createdDate.toEpochMilli(),
                lastUpdateDate = it.lastUpdateDate.toEpochMilli()
            ) },
            createDate = travel.createdDate.toEpochMilli(),
            lastUpdateDate = travel.lastUpdateDate.toEpochMilli()
        )
    }


    fun getAllTravels(retrieveTravelDto: RetrieveTravelDTO): List<TravelDTO> {
        val user = securityContext.findCurrentUser()
        retrieveTravelDto.apply { userKeycloakId = user?.keycloakId }
        val travels = travelRepository.findAllFiltered(retrieveTravelDto)
        return travels.map { travel ->
            TravelDTO(
                id = travel.id,
                destination = travel.destination,
                name = travel.name,
                code = travel.code,
                startDate = travel.startDate!!,
                endDate = travel.endDate!!,
                imageUrl = travel.imageUrl,
                days = travel.days,
                travelDays = null,
                createDate = travel.createdDate.toEpochMilli(),
                lastUpdateDate = travel.lastUpdateDate.toEpochMilli()
            )
        }
    }

    fun getTravelById(travelId: Long): TravelDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel with id $travelId not found")
        return TravelDTO(
            id = travel.id,
            destination = travel.destination,
            name = travel.name,
            code = travel.code,
            startDate = travel.startDate!!,
            endDate = travel.endDate!!,
            imageUrl = travel.imageUrl,
            days = travel.days,
            travelDays = travel.travelDayList.map {TravelDayDTO(
                id = it.id,
                dayNumber = it.dayNumber,
                name = it.name,
                description = it.description,
                travelId = travel.id!!,
                activities = null,
                createDate = it.createdDate.toEpochMilli(),
                lastUpdateDate = it.lastUpdateDate.toEpochMilli()
            ) },
            createDate = travel.createdDate.toEpochMilli(),
            lastUpdateDate = travel.lastUpdateDate.toEpochMilli()
        )
    }
}