package com.planit.mapper

import com.planit.dto.travelAddress.CreateTravelAddressDTO
import com.planit.dto.travelAddress.TravelAddressDTO
import com.planit.dto.travelAddress.UpdateTravelAddressDTO
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
interface TravelAddressMapper {
    fun toDTO(createTravelAddress: CreateTravelAddressDTO) : TravelAddressDTO
    fun toDTO(travelAddress: UpdateTravelAddressDTO) : TravelAddressDTO
}