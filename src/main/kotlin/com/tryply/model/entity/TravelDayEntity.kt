package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class TravelDayEntity : PanacheEntity() {

    var date : String = ""
    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""

    @ManyToOne
    lateinit var travelId : TravelEntity
    @OneToMany(cascade = [(CascadeType.ALL)])
    lateinit var activityDayEntityList : MutableList<ActivityDayEntity>

}