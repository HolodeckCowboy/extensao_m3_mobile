package com.example.extensao

import org.threeten.bp.LocalDateTime

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,
    val enrolledUsers: MutableList<String> = mutableListOf()
)