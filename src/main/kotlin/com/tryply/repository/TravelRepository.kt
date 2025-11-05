package com.tryply.repository

import com.tryply.model.entity.TravelEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TravelRepository : PanacheRepository<TravelEntity> {

}