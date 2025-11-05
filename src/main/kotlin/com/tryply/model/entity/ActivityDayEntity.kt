package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class ActivityDayEntity : PanacheEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var time : String = ""
    var completed : Boolean = false

    @ManyToOne
    var travelDayEntity : TravelDayEntity? = null

}