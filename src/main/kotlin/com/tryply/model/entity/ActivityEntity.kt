package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ActivityEntity : TryPlyEntity() {
    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var completed: Boolean = false

    @ManyToOne
    @JoinColumn(name = "travel_id")
    var travel : TravelEntity? = null
}