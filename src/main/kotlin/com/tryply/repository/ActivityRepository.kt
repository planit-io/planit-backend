package com.tryply.repository

import com.tryply.model.entity.ActivityEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActivityRepository : PanacheRepository<ActivityEntity> {
}