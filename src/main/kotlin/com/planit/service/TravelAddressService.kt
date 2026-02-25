package com.planit.service

import com.planit.dto.travel.TravelDTO
import com.planit.dto.travelAddress.TravelAddressDTO
import com.planit.model.entity.TravelAddress
import com.planit.repository.ActivityDayRepository
import com.planit.repository.ActivityRepository
import com.planit.repository.TravelAddressRepository
import com.planit.repository.TravelRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import org.hibernate.annotations.NotFound

@ApplicationScoped
@Transactional
class TravelAddressService (
    val travelRepository: TravelRepository,
    val travelAddressRepository: TravelAddressRepository,
    val activityDayRepository: ActivityDayRepository,
    val activityRepository: ActivityRepository
) {



    fun createAddress(travelId: Long, travelAddressDTO: TravelAddressDTO) : TravelAddressDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")

        val travelAddress = TravelAddress().apply {
            this.travel = travel
            this.address = travelAddressDTO.address
            this.note = travelAddressDTO.note ?: ""
        }
        if (travelAddressDTO.activityId != null) {
            travelAddress.activity = activityRepository.findById(travelAddressDTO.activityId) ?: throw NotFoundException("Activity not found")
        }

        if (travelAddressDTO.activityDayId != null) {
            travelAddress.activityDay = activityDayRepository.findById(travelAddressDTO.activityDayId) ?: throw NotFoundException("Activity Day not found")
        }

        travelAddress.persist()
        return TravelAddressDTO(
            id = travelAddress.id!!,
            address = travelAddress.address,
            note = travelAddress.note,
            createDate = travelAddress.createdDate.toEpochMilli(),
            lastUpdateDate = travelAddress.lastUpdateDate.toEpochMilli(),
            activityId = travelAddress.activity?.id,
            activityDayId = travelAddress.activityDay?.id,
        )
    }

    fun getAddresses(travelId: Long): List<TravelAddressDTO> {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")
        return travelAddressRepository.list("travel", travel).map { travelAddress ->
            TravelAddressDTO(
                id = travelAddress.id!!,
                address = travelAddress.address,
                note = travelAddress.note,
                createDate = travelAddress.createdDate.toEpochMilli(),
                lastUpdateDate = travelAddress.lastUpdateDate.toEpochMilli(),
                activityId = travelAddress.activity?.id,
                activityDayId = travelAddress.activityDay?.id,
            )
        }
    }

    fun updateAddress(travelId: Long, addressId: Long, travelAddressDTO: TravelAddressDTO) {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")
        val travelAddress = travelAddressRepository.find("travel.id = ?1 and id = ?2", travelId, addressId).firstResult()
            ?: throw NotFoundException("Travel Address not found for id: $addressId in this travel")

        travelAddress.address = travelAddressDTO.address
        travelAddress.note = travelAddressDTO.note ?: ""
        travelAddress.persist()
    }

    fun deleteAddress(travelId: Long, addressId: Long) {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")
        val travelAddress = travelAddressRepository.find("travel.id = ?1 and id = ?2", travelId, addressId).firstResult()
            ?: throw NotFoundException("Travel Address not found for id: $addressId in this travel")
        travelAddress.delete()
    }


}