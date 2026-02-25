package com.planit.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
class TravelAddress : PlanItEntity() {
    var address: String = ""
    var note: String = ""

    @ManyToOne
    lateinit var travel: Travel

    @OneToOne
    var activity : Activity? = null

    @OneToOne
    var activityDay : ActivityDay? = null
}