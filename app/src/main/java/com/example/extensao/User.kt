package com.example.extensao

const val ADMIN_USER = "admin"
const val REGULAR_USER = "user"

data class User(
    val username: String,
    val isAdmin: Boolean
)