package com.planit.service

import com.planit.dto.traveler.TravelerDTO
import com.planit.model.entity.Traveler
import com.planit.model.enums.TravelerRole
import com.planit.repository.TravelRepository
import com.planit.repository.TravelerRepository
import com.planit.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import java.util.logging.Logger

@ApplicationScoped
@Transactional
class TravelerService {
    val travelerRepository : TravelerRepository = TravelerRepository()
    val travelRepository : TravelRepository = TravelRepository()
    val userRepository : UserRepository = UserRepository()

    val logger : Logger = Logger.getLogger(TravelerService::class.java.name)

    fun createTraveler(travelerDTO: TravelerDTO): TravelerDTO {
        val travel = travelRepository.findById(travelerDTO.travelId) ?: throw NotFoundException("Traveler not found")
        val user = userRepository.find("username = ?1", travelerDTO.username).firstResult() ?: throw NotFoundException("User not found")

        if (travelerRepository.find("user.id = ?1 AND travel.id = ?2", user.id, travel.id).firstResult() != null) {
            throw IllegalArgumentException("Traveler already exists for this user and travel")
        }

        val traveler = Traveler().apply {
            this.user = user
            this.travel = travel
            this.role = travelerDTO.role ?: TravelerRole.MEMBER
        }

        traveler.persist()
        return TravelerDTO(
            id = traveler.id!!,
            username = user.username,
            role = traveler.role,
            travelId = travel.id!!,
            createdDate = traveler.createdDate.toEpochMilli(),
            lastUpdatedDate = traveler.lastUpdateDate.toEpochMilli()
        )

    }

    fun getTravelers(): List<TravelerDTO> {
        return travelerRepository.listAll().map { traveler ->
            TravelerDTO(
                id = traveler.id!!,
                username = traveler.user.username,
                role = traveler.role,
                travelId = traveler.travel.id!!,
                createdDate = traveler.createdDate.toEpochMilli(),
                lastUpdatedDate = traveler.lastUpdateDate.toEpochMilli()
            )
        }
    }

    fun getTravelerById(travelerId: Long): TravelerDTO {
        val traveler = travelerRepository.findById(travelerId) ?: throw NotFoundException("Traveler not found")
        return TravelerDTO(
            id = traveler.id!!,
            username = traveler.user.username,
            role = traveler.role,
            travelId = traveler.travel.id!!,
            createdDate = traveler.createdDate.toEpochMilli(),
            lastUpdatedDate = traveler.lastUpdateDate.toEpochMilli()
        )
    }

    fun updateTraveler(travelerId: Long, role: TravelerRole) {
        val traveler = travelerRepository.findById(travelerId) ?: throw NotFoundException("Traveler not found")
        traveler.role = role
        traveler.persistAndFlush()
    }

    fun deleteTraveler(travelerId: Long) {
        val traveler = travelerRepository.findById(travelerId) ?: throw NotFoundException("Traveler not found")
        traveler.delete()
    }

}