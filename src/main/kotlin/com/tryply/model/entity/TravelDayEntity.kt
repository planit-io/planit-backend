package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import kotlin.collections.mutableListOf

@Entity
class TravelDayEntity : TryPlyEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var dayNumber : Int = 0

    @ManyToOne
    lateinit var travel : TravelEntity
    @OneToMany(cascade = [(CascadeType.ALL)])
    var activityDayEntityList = mutableListOf<ActivityDayEntity>()

}