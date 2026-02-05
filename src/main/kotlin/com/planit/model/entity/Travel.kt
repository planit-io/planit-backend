package com.planit.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import java.time.LocalDate
import java.util.*

@Entity
class Travel : PlanItEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var destination: String = ""
    var code: String = ""
    @Column(columnDefinition = "DATE")
    var startDate: LocalDate? = null
    @Column(columnDefinition = "DATE")
    var endDate: LocalDate? = null
    var imageUrl: String? = null
    var days: Int = 1
    @OneToMany(cascade = [(CascadeType.ALL)])
    var travelDayList = mutableListOf<TravelDay>()
    @OneToMany(cascade = [(CascadeType.ALL)])
    var activityList = mutableListOf<Activity>()
    @OneToMany(cascade = [(CascadeType.ALL)])
    var costList = mutableListOf<Cost>()
    @OneToMany(cascade = [(CascadeType.ALL)], mappedBy = "travel")
    var travelerList = mutableListOf<Traveler>()


    override fun toString(): String {
        return "TravelEntity(id=$id, destination='$destination', name='$name', code=$code, startDate='$startDate', endDate='$endDate')"
    }

    fun generateCode() {
        this.code = UUID.randomUUID().toString()
    }

    fun generateTravelDays() {
        travelDayList.clear()
        // Calculate delta days between start and end date
        var deltaDays = endDate?.toEpochDay()?.minus(startDate?.toEpochDay() ?: 0) ?: 0

        days = deltaDays.toInt() + 1
        for (i in 1..days) {
            val travelDay = TravelDay()
            travelDay.dayNumber = i
            travelDay.name = "Day $i"
            travelDay.description = ""
            travelDay.travel = this
            travelDayList.add(travelDay)
        }
    }
}