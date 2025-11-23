package com.tryply.dto.refund

import com.tryply.dto.costunit.CreateCostUnitDTO
import com.tryply.model.enums.CostType
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateRefundDTO (
    val reason: String,

    @field:Positive
    val totalAmount: Double,

    @field:Size(min = 2, max = 4)
    val currency: String,

    val costUnitList : List<CreateCostUnitDTO>,

    @field:Size(min = 1)
    val payers : List<String>,

    @field:Size(min = 3, max = 50)
    val payedBy : String,

    val date : Long?,

    val travelId : Long,

    val costType: CostType
)