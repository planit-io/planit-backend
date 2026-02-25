package com.planit.resource

import com.planit.dto.travelAddress.CreateTravelAddressDTO
import com.planit.dto.travelAddress.TravelAddressDTO
import com.planit.dto.travelAddress.UpdateTravelAddressDTO
import com.planit.mapper.TravelAddressMapper
import com.planit.service.TravelAddressService
import com.planit.validator.TravelAddressValidator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/api/travels/{travelId}/addresses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class TravelAddressResource (
    val travelAddressMapper: TravelAddressMapper,
    val travelAddressService: TravelAddressService
) {
    val travelAddressValidator = TravelAddressValidator()

    @POST
    fun createAddress(
        @PathParam("travelId") travelId: Long,
        createTravelAddressDTO: CreateTravelAddressDTO
    ) : TravelAddressDTO {
        val travelDTO = travelAddressMapper.toDTO(createTravelAddressDTO)
        travelAddressValidator.validateTravelAddressDTO(travelDTO)
        return travelAddressService.createAddress(travelId, travelDTO)
    }

    @GET
    fun getAddresses(@PathParam("travelId") travelId: Long): List<TravelAddressDTO> {
        return travelAddressService.getAddresses(travelId)
    }

    @PUT
    @Path("/{addressId}")
    fun updateAddress(
        @PathParam("travelId") travelId: Long,
        @PathParam("addressId") addressId: Long,
        updateTravelAddressDTO: UpdateTravelAddressDTO
    ) {
        val travelAddressDTO = travelAddressMapper.toDTO(updateTravelAddressDTO)
        travelAddressValidator.validateTravelAddressDTO(travelAddressDTO)
        travelAddressService.updateAddress(travelId, addressId, travelAddressDTO)
    }

    @DELETE
    @Path("/{addressId}")
    fun deleteAddress(
        @PathParam("travelId") travelId: Long,
        @PathParam("addressId") addressId: Long
    ) {
        travelAddressService.deleteAddress(travelId, addressId)
    }
}