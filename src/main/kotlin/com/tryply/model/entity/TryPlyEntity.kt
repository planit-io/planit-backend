package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostPersist
import jakarta.persistence.PrePersist

@MappedSuperclass
open class TryPlyEntity : PanacheEntity() {

    var createdDate : Long = System.currentTimeMillis()
    var lastUpdateDate : Long = System.currentTimeMillis()

    @PrePersist
    fun setup() {
        createdDate = System.currentTimeMillis()
    }

    @PostPersist
    fun update(){
        lastUpdateDate = System.currentTimeMillis()
    }
}