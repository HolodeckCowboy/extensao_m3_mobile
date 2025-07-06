package com.example.extensao

import org.threeten.bp.LocalDateTime

object FakeDataSource {
    private val events = mutableListOf<Event>()
    private val users = listOf(
        User(REGULAR_USER, false),
        User(ADMIN_USER, true)
    )
    private var nextId = 3

    init {
        // Add sample events
        events.add(Event(
            1,
            "Team Meeting",
            "Weekly team sync",
            LocalDateTime.now().plusDays(1)
        ))
        events.add(Event(
            2,
            "Workshop",
            "Android development workshop",
            LocalDateTime.now().plusDays(3)
        ))
    }

    fun getEventsForUser(username: String) = events.filter {
        it.enrolledUsers.contains(username) || username == ADMIN_USER
    }

    fun getAllEvents() = events.toList()

    fun addEvent(event: Event) {
        events.add(event.copy(id = nextId++))
    }

    fun authenticate(username: String, password: String): User? {
        return users.find { it.username == username && password == username }
    }

    fun enrollUser(eventId: Int, username: String) {
        events.find { it.id == eventId }?.enrolledUsers?.add(username)
    }
}