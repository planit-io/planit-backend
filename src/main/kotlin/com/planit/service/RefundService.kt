package com.planit.service

import com.planit.dto.cost.CostDTO
import com.planit.dto.costunit.CostUnitDTO
import com.planit.model.entity.Cost
import com.planit.model.entity.CostUnit
import com.planit.repository.CostRepository
import com.planit.repository.CostUnitRepository
import com.planit.repository.TravelRepository
import com.planit.repository.TravelerRepository
import com.planit.utils.CostUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException

@ApplicationScoped
@Transactional
class RefundService {

    private val costRepository = CostRepository()
    private val costUnitRepository = CostUnitRepository()
    private val travelRepository = TravelRepository()
    private val travelerRepository = TravelerRepository()
    private val costUtils = CostUtils()


    fun calculateRefunds(travelId: Long): List<CostDTO> {
        val costs = costRepository.find("travel.id", travelId).list()
        if (costs.isEmpty()) {
            return emptyList()
        }
        return costUtils.calculateRefundsAndPayments(costs)
    }

    fun createRefund(travelId: Long, refund: CostDTO): CostDTO {
        val travel = travelRepository.findById(travelId) ?: throw NotFoundException("Travel not found for id: $travelId")
        val payers = travelerRepository.find("travel.id = ?1 and user.username in ?2", travelId, refund.payers).list()
        val payerTraveler = travelerRepository.find("travel.id = ?1 AND user.username = ?2", travelId, refund.payedBy).firstResult()
            ?: throw NotFoundException("Payer traveler not found in this travel for username: ${refund.payedBy}")

        if (payers.isEmpty() || payers.size != refund.payers.size){
            throw IllegalArgumentException("No valid payers found in the travel for usernames: ${refund.payers}")
        }

        val cost = Cost().apply {
            this.reason = refund.reason
            this.amount = refund.totalAmount
            this.currency = refund.currency
            this.date = refund.date ?: System.currentTimeMillis()
            this.payer = payerTraveler
            this.travel = travel
            this.costType = refund.costType
        }


        if (refund.costUnitList.isEmpty()){
            cost.costs = costUtils.generateCostsUnitsEvenly(cost, payers) as MutableList<CostUnit>
        }
        else {
            cost.costs = costUtils.generateCostUnitWithCustomAmount(cost, refund.costUnitList, payers) as MutableList<CostUnit>
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
}