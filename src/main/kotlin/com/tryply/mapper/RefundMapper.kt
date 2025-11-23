package com.tryply.mapper

import com.tryply.dto.cost.CostDTO
import com.tryply.dto.refund.CreateRefundDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
interface RefundMapper {

    @Mapping(target = "costType", defaultValue = "REFUND")
    fun toDTO(costDTO: CreateRefundDTO) : CostDTO
}