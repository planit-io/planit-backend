package com.planit.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

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


    @OneToOne(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "activityDay")
    var travelAddress : TravelAddress? = null

}