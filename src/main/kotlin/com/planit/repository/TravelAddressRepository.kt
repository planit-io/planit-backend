package com.planit.repository

import com.planit.dto.travelAddress.TravelAddressDTO
import com.planit.model.entity.TravelAddress
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TravelAddressRepository : PanacheRepository<TravelAddress> {
}