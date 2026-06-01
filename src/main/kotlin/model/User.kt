package com.cyh.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val hashPassword: String,
    val email: String,
    val phone: String
)