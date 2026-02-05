package com.planit.service

import com.planit.dto.user.UserDTO
import com.planit.model.entity.User
import com.planit.repository.UserRepository
import com.planit.validator.UserValidator
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException

@ApplicationScoped
@Transactional
class UserService {
    val logger = LoggerFactory.getLogger(UserService::class.java)
    val userRepository = UserRepository()

    fun createUser(userDTO: UserDTO): UserDTO {

        logger.info("Creating user: ${userDTO.username}")
        val validator = UserValidator()
        validator.validateUserData(userDTO)

        var user = userRepository.find("email", userDTO.email).firstResult()

        if (user == null){
            user = User().apply {
                keycloakId = userDTO.keycloakId ?: ""
                username = userDTO.username
                email = userDTO.email
                firstName = userDTO.firstName
                lastName = userDTO.lastName
                profilePictureUrl = userDTO.profilePictureUrl
            }
        }
        else {
            user.keycloakId = userDTO.keycloakId ?: ""
            user.username = userDTO.username
            user.firstName = userDTO.firstName
            user.lastName = userDTO.lastName
            user.profilePictureUrl = userDTO.profilePictureUrl
        }

        user.persist()
        logger.info("Created user: ${userDTO.username}")
        return UserDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            profilePictureUrl = user.profilePictureUrl,
            createDate = user.createdDate.toEpochMilli(),
            lastUpdateDate = user.lastUpdateDate.toEpochMilli(),
            keycloakId = user.keycloakId,
        )
    }

    fun getAllUsers(): List<UserDTO> {
        return userRepository.listAll().map { user ->
            UserDTO(
                id = user.id,
                username = user.username,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                profilePictureUrl = user.profilePictureUrl,
                createDate = user.createdDate.toEpochMilli(),
                lastUpdateDate = user.lastUpdateDate.toEpochMilli(),
                keycloakId = user.keycloakId
            )
        }
    }

    fun getUserById(userId: Long): UserDTO {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User with id $userId not found")
        return UserDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            profilePictureUrl = user.profilePictureUrl,
            createDate = user.createdDate.toEpochMilli(),
            lastUpdateDate = user.lastUpdateDate.toEpochMilli(),
            keycloakId = user.keycloakId
        )
    }
}