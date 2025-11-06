package com.tryply.repository

import com.tryply.model.entity.ActivityDayEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActivityDayRepository : PanacheRepository<ActivityDayEntity> {
}