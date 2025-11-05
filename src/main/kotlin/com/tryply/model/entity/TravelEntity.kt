package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
class TravelEntity : PanacheEntity() {

    var name: String = ""
    @Column(columnDefinition = "TEXT")
    var destination: String = ""
    var code: String = ""
    var startDate: String = ""
    var endDate: String = ""
    var imageUrl: String? = null
    @OneToMany(cascade = [(CascadeType.ALL)])
    var travelDayEntityList = mutableListOf<TravelDayEntity>()
    @OneToMany(cascade = [(CascadeType.ALL)])
    var activityEntityList = mutableListOf<ActivityEntity>()


    override fun toString(): String {
        return "TravelEntity(id=$id, destination='$destination', name='$name', code=$code, startDate='$startDate', endDate='$endDate')"
    }

    fun generateCode() {
        this.code = UUID.randomUUID().toString()
    }
}