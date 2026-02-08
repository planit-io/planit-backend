package com.planit.repository

import com.planit.dto.travel.RetrieveTravelDTO
import com.planit.model.entity.Travel
import com.planit.model.entity.Traveler
import com.planit.model.entity.User
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate

@ApplicationScoped
class TravelRepository : PanacheRepository<Travel> {

    @Inject
    lateinit var em: EntityManager

    fun findAllFiltered(retrieveTravelDto : RetrieveTravelDTO): List<Travel> {


        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Travel::class.java)
        val travel = cq.from(Travel::class.java)

        // Build dynamic query based on filters in retrieveTravelDto

        val predicates = mutableListOf<Predicate>()

        retrieveTravelDto.destination?.let {
            predicates.add(cb.like(travel.get<String>("destination"), "%$it%"))
        }
        retrieveTravelDto.name?.let {
            predicates.add(cb.like(travel.get<String>("name"), "%$it%"))
        }

        retrieveTravelDto.startDate?.let {
            predicates.add(cb.greaterThanOrEqualTo(travel.get("startDate"), it))
        }
        retrieveTravelDto.endDate?.let {
            predicates.add(cb.lessThanOrEqualTo(travel.get("endDate"), it))
        }

        retrieveTravelDto.userKeycloakId?.let {
            val travelerJoin : Join<Travel, Traveler> = travel.join("travelerList", JoinType.LEFT)
            val userJoin : Join<Traveler, User> = travelerJoin.join("user")
            predicates.add(cb.equal(userJoin.get<String>("keycloakId"), it))
        }

        cq.select(travel)
            .distinct(true)
            .where(*predicates.toTypedArray())


        return em.createQuery(cq).resultList
    }

}