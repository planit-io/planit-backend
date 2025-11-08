package com.tryply.model.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrePersist

@Entity
class UserEntity : TryPlyEntity() {

    @Column(unique = true, nullable = false)
    var username: String = ""
    var firstName: String = ""
    var lastName: String = ""
    @Column(unique = true, nullable = false)
    var email: String = ""
    var profilePictureUrl: String? = null

    override fun toString(): String {
        return "UserEntity(id=$id, username='$username', firstName='$firstName', lastName='$lastName', email='$email', profilePictureUrl=$profilePictureUrl)"
    }
}