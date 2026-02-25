package com.planit.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
class Activity : PlanItEntity() {
    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var description: String = ""
    var completed: Boolean = false

    @ManyToOne
    @JoinColumn(name = "travel_id")
    var travel : Travel? = null

    @OneToOne(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "activity")
    var travelAddress : TravelAddress? = null
}