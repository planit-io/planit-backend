package com.tryply.resource

import com.tryply.dto.cost.CostDTO
import com.tryply.dto.refund.CreateRefundDTO
import com.tryply.mapper.RefundMapper
import com.tryply.service.RefundService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*

@ApplicationScoped
@Path("travels/{travelId}/refunds")
@Produces("application/json")
@Consumes("application/json")
class RefundResource {

    @Inject
    private lateinit var refundService: RefundService

    @Inject
    private lateinit var refundMapper: RefundMapper


    @GET
    fun getRefunds(
        @PathParam("travelId") travelId: Long
    ): List<CostDTO> {
        return refundService.calculateRefunds(travelId)
    }

    @POST
    fun createRefund(
        @PathParam("travelId") travelId: Long,
        @Valid refundDTO: CreateRefundDTO
    ): CostDTO {
        val refund = refundMapper.toDTO(refundDTO)
        return refundService.createRefund(travelId, refund)
    }

}