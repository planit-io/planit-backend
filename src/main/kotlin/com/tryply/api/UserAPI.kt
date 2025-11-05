package com.tryply.api

import com.tryply.model.dto.UserDTO
import com.tryply.model.entity.UserEntity
import com.tryply.model.validator.UserValidator
import com.tryply.repository.UserRepository
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class UserAPI {

    val logger = LoggerFactory.getLogger(UserAPI::class.java)
    val userRepository = UserRepository()

    @POST
    fun createUser(userDTO: UserDTO): UserDTO {

        logger.info("Creating user: ${userDTO.username}")
        val validator = UserValidator()
        validator.validateUserData(userDTO)

        userRepository.find("email", userDTO.email).firstResult()?.let {
            throw IllegalArgumentException("Email already in use")
        }
        userRepository.find("username", userDTO.username).firstResult()?.let {
            throw IllegalArgumentException("Username already in use")
        }

        val user = UserEntity().apply {
            username = userDTO.username
            email = userDTO.email
            firstName = userDTO.firstName
            lastName = userDTO.lastName
            profilePictureUrl = userDTO.profilePictureUrl
        }

        user.persist()
        logger.info("Created user: ${userDTO.username}")
        return UserDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            profilePictureUrl = user.profilePictureUrl
        )
    }

    @GET
    fun getAllUsers(): List<UserDTO> {
        return userRepository.listAll().map { user ->
            UserDTO(
                id = user.id,
                username = user.username,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                profilePictureUrl = user.profilePictureUrl
            )
        }
    }

    @GET
    @Path("/{userId}")
    fun getUserById(@PathParam("userId") userId: Long): UserDTO {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User with id $userId not found")
        return UserDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            profilePictureUrl = user.profilePictureUrl
        )
    }

}