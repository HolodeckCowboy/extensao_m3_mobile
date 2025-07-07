package com.example.extensao

import java.io.Serializable

data class Event(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val description: String,
    var isUserEnrolled: Boolean = false
) : Serializable