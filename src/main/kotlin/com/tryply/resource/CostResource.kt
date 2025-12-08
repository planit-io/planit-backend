package com.tryply.resource

import com.tryply.dto.cost.CostDTO
import com.tryply.dto.cost.CreateCostDTO
import com.tryply.dto.cost.UpdateCostDTO
import com.tryply.mapper.CostMapper
import com.tryply.mapper.CostUnitMapper
import com.tryply.service.CostService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement

@ApplicationScoped
@Path("/api/travel/{travelId}/costs")
@Produces("application/json")
@Consumes("application/json")
class CostResource {


    val costService : CostService
    val costMapper : CostMapper
    val costUnitMapper : CostUnitMapper

    @Inject
    constructor(costService: CostService, costMapper: CostMapper, costUnitMapper: CostUnitMapper) {
        this.costService = costService
        this.costMapper = costMapper
        this.costUnitMapper = costUnitMapper
    }

    @POST
    @Path("/preview")
    fun addCostPreview(
        @PathParam("travelId") travelId : Long,
        @Valid costDTO: CreateCostDTO
    ): CostDTO {
        val cost = costMapper.toDTO(costDTO)
        return costService.addCostPreview(travelId, cost)
    }

    @POST
    fun addCost(
        @PathParam("travelId") travelId : Long,
        @Valid costDTO: CreateCostDTO
        ): CostDTO {
        val cost = costMapper.toDTO(costDTO)
        return costService.createCost(travelId, cost)
    }

    @GET
    fun getCosts(
        @PathParam("travelId") travelId : Long,
        ): List<CostDTO> {
        // Implementation for retrieving costs goes here
        return costService.getCosts(travelId)
    }

    @GET
    @Path("/{costId}")
    fun getCostById(@PathParam("travelId") travelId : Long,
                    @PathParam("costId") costId : Long,
    ): CostDTO {
        // Implementation for retrieving a cost by ID goes here
        return costService.getCostById(travelId, costId)
    }

    @PUT
    @Path("/{costId}")
    fun updateCost(@PathParam("travelId") travelId : Long,
                   @PathParam("costId") costId : Long,
                     costDTO: UpdateCostDTO
    ) {
        // Implementation for updating a cost goes here
        val cost = costMapper.toDTO(costDTO)
        costService.updateCost(travelId, costId, cost)
    }


    @DELETE
    @Path("/{costId}")
    fun deleteCost(
        @PathParam("travelId") travelId : Long,
        @PathParam("costId") costId : Long,
        ) {

        costService.deleteCost(travelId, costId)
    }

}