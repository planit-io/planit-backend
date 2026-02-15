package com.planit.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import kotlin.collections.mutableListOf

@Entity
class TravelDay : PlanItEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var dayNumber : Int = 0

    @ManyToOne
    lateinit var travel : Travel
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "travelDay")
    var activityDayList = mutableListOf<ActivityDay>()

}