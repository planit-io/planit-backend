package com.tryply.repository

import com.tryply.model.entity.TravelDayEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TravelDayRepository : PanacheRepository<TravelDayEntity> {
}