package com.planit.model.entity

import com.planit.listener.AuditEntityListener
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditEntityListener::class)
open class PlanItEntity : PanacheEntity() {

    @CreationTimestamp
    var createdDate : Instant = Instant.now()
    @UpdateTimestamp
    var lastUpdateDate : Instant = Instant.now()
    var createdBy : String = ""
    var updatedBy : String = ""

}