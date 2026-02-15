package com.planit.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ActivityDay : PlanItEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var time : String? = ""
    var completed : Boolean = false

    //TODO: add order field to sort activities in a day

    @ManyToOne
    @JoinColumn(name = "travel_day_id")
    var travelDay : TravelDay? = null

}