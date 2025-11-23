package com.tryply.utils

import com.tryply.dto.cost.CostDTO
import com.tryply.dto.costunit.CostUnitDTO
import com.tryply.model.entity.Cost
import com.tryply.model.entity.CostUnit
import com.tryply.model.entity.Traveler
import com.tryply.model.enums.CostType
import java.math.BigDecimal
import java.math.RoundingMode


class CostUtils {

    fun generateCostsUnitsEvenly(
        cost: CostDTO,
        travelers: List<Traveler>
    ): List<CostUnit> {
        val costUnits = mutableListOf<CostUnit>()
        val splitAmount = BigDecimal(cost.totalAmount / travelers.size, ).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        for (traveler in travelers) {
            val costUnit = CostUnit().apply {
                this.amount = splitAmount
                this.currency = cost.currency
                this.traveler = traveler
            }
            costUnits.add(costUnit)
        }

        return costUnits
    }


    fun generateCostsUnitsEvenly(
        cost: Cost,
        travelers: List<Traveler>
    ): List<CostUnit> {
        val costUnits = mutableListOf<CostUnit>()
        val splitAmount = BigDecimal(cost.amount / travelers.size, ).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        for (traveler in travelers) {
            val costUnit = CostUnit().apply {
                this.amount = splitAmount
                this.currency = cost.currency
                this.traveler = traveler
                this.cost = cost
            }
            costUnits.add(costUnit)
        }

        return costUnits
    }


    fun generateCostUnitWithCustomAmount(
        cost : CostDTO,
        costUnitList: List<CostUnitDTO>,
        travelers: List<Traveler>
    ) : List<CostUnit> {
        val costUnits = mutableListOf<CostUnit>()

        for (costUnitDTO in costUnitList) {
            val traveler = travelers.find { it.user.username == costUnitDTO.travelerUsername }
                ?: continue

            val costUnit = CostUnit().apply {
                this.amount = costUnitDTO.amount ?: 0.0
                this.currency = cost.currency
                this.traveler = traveler
            }
            costUnits.add(costUnit)
        }

        return costUnits
    }

    fun generateCostUnitWithCustomAmount(
        cost : Cost,
        costUnitList: List<CostUnitDTO>,
        travelers: List<Traveler>
    ) : List<CostUnit> {
        val costUnits = mutableListOf<CostUnit>()

        for (costUnitDTO in costUnitList) {
            val traveler = travelers.find { it.user.username == costUnitDTO.travelerUsername }
                ?: continue

            val costUnit = CostUnit().apply {
                this.amount = costUnitDTO.amount ?: 0.0
                this.currency = cost.currency
                this.traveler = traveler
                this.cost = cost
            }
            costUnits.add(costUnit)
        }

        return costUnits
    }


    fun calculateRefundsAndPayments(costList: List<Cost>): List<CostDTO> {
        val travelId = if (costList.isNotEmpty()) costList[0].travel.id!! else throw IllegalArgumentException("Cost list is empty")
        var balances = mutableMapOf<String, Double>()
        for (cost in costList) {
            for (costUnit in cost.costs) {
                val username = costUnit.traveler.user.username
                balances[username] = balances.getOrDefault(username, 0.0) - costUnit.amount
            }
            val payerUsername = cost.payer.user.username
            balances[payerUsername] = balances.getOrDefault(payerUsername, 0.0) + cost.amount
        }

        var positiveBalances = balances.filter { it.value > 0 }.toList().sortedByDescending { (_, v) -> v }.toMap().keys
        var negativeBalances = balances.filter { it.value < 0 }.toList().sortedBy { (_, v) -> v }.toMap().keys

        val transactions = mutableListOf<CostDTO>()

        var i = 0
        for (positiveTraveler in positiveBalances) {
            while (balances[positiveTraveler]!! > 0 && i < negativeBalances.size) {
                val negativeTraveler = negativeBalances.elementAt(i)
                val paymentAmount = minOf(balances[positiveTraveler]!!, -balances[negativeTraveler]!!)

                val costDTO = CostDTO(
                    reason = "Settlement Payment",
                    totalAmount = paymentAmount,
                    currency = "USD",
                    costUnitList = listOf(
                        CostUnitDTO(
                            travelerUsername = positiveTraveler,
                            amount = paymentAmount,
                            currency = "USD",
                            id = null,
                            costId = null
                        )
                    ),
                    payedBy = negativeTraveler,
                    travelId = travelId,
                    costType = CostType.REFUND,
                    createdDate = System.currentTimeMillis(),
                    lastUpdatedDate = System.currentTimeMillis(),
                    payers = listOf(positiveTraveler),
                    id = null,
                    date = null,
                )
                transactions.add(costDTO)

                balances[positiveTraveler] = balances[positiveTraveler]!! - paymentAmount
                balances[negativeTraveler] = balances[negativeTraveler]!! + paymentAmount

                if (balances[negativeTraveler] == 0.0) {
                    i++
                }
            }
        }
        return transactions
    }


}