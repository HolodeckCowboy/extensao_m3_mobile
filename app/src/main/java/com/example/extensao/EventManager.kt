package com.example.extensao

import android.content.Context
import com.example.extensao.R

object EventManager {

    private val events = mutableListOf<Event>()

    // A check to prevent adding events multiple times
    private var eventsInitialized = false

    fun initializeEvents(context: Context) {
        if (eventsInitialized) return
        events.clear()
        events.addAll(listOf(
            Event(1, context.getString(R.string.event1_name), "2025-08-15", "09:00", context.getString(R.string.event1_desc), isUserEnrolled = true),
            Event(2, context.getString(R.string.event2_name), "2025-09-10", "10:00", context.getString(R.string.event2_desc), isUserEnrolled = false),
            Event(3, context.getString(R.string.event3_name), "2025-09-22", "14:00", context.getString(R.string.event3_desc), isUserEnrolled = true),
            Event(4, context.getString(R.string.event4_name), "2025-10-05", "11:00", context.getString(R.string.event4_desc), isUserEnrolled = false)
        ))
        eventsInitialized = true
    }

    fun getAllEvents(): List<Event> = events

    fun getEnrolledEvents(): List<Event> = events.filter { it.isUserEnrolled }

    fun addEvent(event: Event) {
        events.add(event)
    }

    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }
}