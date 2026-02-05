package com.planit.service

import com.planit.dto.cost.CostDTO
import com.planit.dto.costunit.CostUnitDTO
import com.planit.model.entity.Cost
import com.planit.model.entity.CostUnit
import com.planit.model.enums.CostType
import com.planit.repository.CostRepository
import com.planit.repository.CostUnitRepository
import com.planit.repository.TravelRepository
import com.planit.repository.TravelerRepository
import com.planit.utils.CostUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import java.util.logging.Logger

@ApplicationScoped
@Transactional
class CostService {

    val travelRepository : TravelRepository = TravelRepository()
    val travelerRepository : TravelerRepository = TravelerRepository()
    val costRepository : CostRepository = CostRepository()
    val costUnitRepository : CostUnitRepository = CostUnitRepository()
    val logger = Logger.getLogger(CostService::class.java.name)
    val costUtils = CostUtils()

    fun addCostPreview(travelId: Long, costDTO: CostDTO): CostDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")
        val payers = travelerRepository.find("travel.id = ?1 and user.username in ?2", travelId, costDTO.payers).list()
        val payerTraveler = travelerRepository.find("travel.id = ?1 AND user.username = ?2", travelId, costDTO.payedBy).firstResult()
            ?: throw NotFoundException("Payer traveler not found in this travel for username: ${costDTO.payedBy}")

        if (payers.isEmpty() || payers.size != costDTO.payers.size){
            throw IllegalArgumentException("No valid payers found in the travel for usernames: ${costDTO.payers}")
        }

        var costUnitList : List<CostUnit>

        if (costDTO.costUnitList.isEmpty()){
            costUnitList = costUtils.generateCostsUnitsEvenly(costDTO, payers)
        }
        else {
            costUnitList = costUtils.generateCostUnitWithCustomAmount(costDTO, costDTO.costUnitList, payers)
        }

        return CostDTO(
            reason = costDTO.reason,
            totalAmount = costDTO.totalAmount,
            currency = costDTO.currency,
            date = costDTO.date,
            costUnitList = costUnitList.map { costUnit ->
                CostUnitDTO(
                    travelerUsername = costUnit.traveler.user.username,
                    amount = costUnit.amount,
                    currency = costUnit.currency,
                    id = null,
                    costId = null
                )
            },
            payedBy = payerTraveler.user.username,
            id = null,
            createdDate = 0,
            lastUpdatedDate = 0,
            travelId = travel.id!!,
            payers = payers.map { it.user.username },
            costType = CostType.COST
        )

    }



    fun createCost(
        travelId : Long,
        costDTO: CostDTO
    ): CostDTO {

        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found for id: $travelId")
        val payers = travelerRepository.find("travel.id = ?1 and user.username in ?2", travelId, costDTO.payers).list()
        val payerTraveler = travelerRepository.find("travel.id = ?1 AND user.username = ?2", travelId, costDTO.payedBy).firstResult()
            ?: throw NotFoundException("Payer traveler not found in this travel for username: ${costDTO.payedBy}")

        if (payers.isEmpty() || payers.size != costDTO.payers.size){
            throw IllegalArgumentException("No valid payers found in the travel for usernames: ${costDTO.payers}")
        }

        val cost = Cost().apply {
            this.reason = costDTO.reason
            this.amount = costDTO.totalAmount
            this.currency = costDTO.currency
            this.date = costDTO.date ?: System.currentTimeMillis()
            this.payer = payerTraveler
            this.travel = travel
        }


        if (costDTO.costUnitList.isEmpty()){
            cost.costs = costUtils.generateCostsUnitsEvenly(cost, payers) as MutableList<CostUnit>
        }
        else {
            cost.costs = costUtils.generateCostUnitWithCustomAmount(cost, costDTO.costUnitList, payers) as MutableList<CostUnit>
        }

        cost.persist()

        return CostDTO(
            reason = cost.reason,
            totalAmount = cost.amount,
            currency = cost.currency,
            costUnitList = cost.costs.map { costUnit ->
                CostUnitDTO(
                    travelerUsername = costUnit.traveler.user.username,
                    amount = costUnit.amount,
                    currency = costUnit.currency,
                    id = costUnit.id!!,
                    costId = costUnit.cost.id!!
                )
            },
            payedBy = cost.payer.user.username,
            createdDate = cost.createdDate.toEpochMilli(),
            lastUpdatedDate = cost.lastUpdateDate.toEpochMilli(),
            id = cost.id!!,
            date = cost.date,
            travelId = travel.id!!,
            payers = cost.costs.filter { it.amount > 0.0 }.map { it.traveler.user.username },
            costType = cost.costType
        )
    }

    fun getCosts(travelId: Long): List<CostDTO> {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found for id: $travelId")
        val costList = costRepository.find("travel.id = ?1", travelId).list()
        return costList.map { cost ->
            CostDTO(
                reason = cost.reason,
                totalAmount = cost.amount,
                currency = cost.currency,
                costUnitList = cost.costs.map { costUnit ->
                    CostUnitDTO(
                        travelerUsername = costUnit.traveler.user.username,
                        amount = costUnit.amount,
                        currency = costUnit.currency,
                        id = costUnit.id!!,
                        costId = cost.id!!
                    )
                },
                payedBy = cost.payer.user.username,
                createdDate = cost.createdDate.toEpochMilli(),
                lastUpdatedDate = cost.lastUpdateDate.toEpochMilli(),
                id = cost.id!!,
                date = cost.date,
                travelId = travel.id!!,
                payers = cost.costs.filter { it.amount > 0.0 }.map { it.traveler.user.username },
                costType = cost.costType
            )
        }
    }

    fun getCostById(travelId: Long, costId : Long): CostDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found")
        val cost = costRepository.find("travel.id = ?1 AND id = ?2", travelId, costId).firstResult()
            ?: throw NotFoundException("Cost not found for travel id: $travelId and cost id: $costId")
        return CostDTO(
            reason = cost.reason,
            totalAmount = cost.amount,
            currency = cost.currency,
            costUnitList = cost.costs.map { costUnit ->
                CostUnitDTO(
                    travelerUsername = costUnit.traveler.user.username,
                    amount = costUnit.amount,
                    currency = costUnit.currency,
                    id = costUnit.id!!,
                    costId = cost.id!!
                )
            },
            payedBy = cost.payer.user.username,
            createdDate = cost.createdDate.toEpochMilli(),
            lastUpdatedDate = cost.lastUpdateDate.toEpochMilli(),
            id = cost.id!!,
            date = cost.date,
            travelId = travel.id!!,
            payers = cost.costs.map { it.traveler.user.username },
            costType = cost.costType
        )
    }

    fun updateCost(travelId: Long, costId: Long, costDTO: CostDTO) {
        val cost = costRepository.find("travel.id = ?1 AND id = ?2", travelId, costId).firstResult()
            ?: throw NotFoundException("Cost not found for travel id: $travelId and cost id: $costId")
        val payerTraveler = travelerRepository.find("travel.id = ?1 AND user.username = ?2", travelId, costDTO.payedBy).firstResult()
            ?: throw NotFoundException("Payer traveler not found in this travel for username: ${costDTO.payedBy}")

        val payers = travelerRepository.find("travel.id = ?1 and user.username in ?2", travelId, costDTO.payers).list()
        if (payers.isEmpty() || payers.size != costDTO.payers.size) {
            throw IllegalArgumentException("No valid payers found in the travel for usernames: ${costDTO.payers}")
        }

        cost.apply {
            this.reason = costDTO.reason
            this.amount = costDTO.totalAmount
            this.currency = costDTO.currency
            this.date = costDTO.date ?: this.date
            this.payer = payerTraveler
        }

        val desiredUnits: List<CostUnit> = if (costDTO.costUnitList.isEmpty()) {
            costUtils.generateCostsUnitsEvenly(cost, payers)
        } else {
            costUtils.generateCostUnitWithCustomAmount(cost, costDTO.costUnitList, payers)
        }

        val existingByUsername = cost.costs.associateBy { it.traveler.user.username }.toMutableMap()

        for (desired in desiredUnits) {
            val username = desired.traveler.user.username
            val existing = existingByUsername.remove(username)
            if (existing != null) {
                existing.amount = desired.amount
                existing.currency = desired.currency

            } else {
                desired.cost = cost
                cost.costs.add(desired)
            }
        }

        for (toRemove in existingByUsername.values) {
            toRemove.amount = 0.0
        }

        cost.persist()
    }


    fun deleteCost(travelId: Long, costId: Long) {
        val cost = costRepository.find("travel.id = ?1 AND id = ?2", travelId, costId).firstResult()
            ?: throw NotFoundException("Cost not found for travel id: $travelId and cost id: $costId")
        cost.delete()
    }



}