package com.example.extensao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter

class DashboardActivity : AppCompatActivity() {
    private lateinit var calendarTextView: TextView
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var syncButton: Button
    private lateinit var createEventButton: Button
    private lateinit var contactUsButton: Button

    private lateinit var username: String
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        calendarTextView = findViewById(R.id.calendarTextView)
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        syncButton = findViewById(R.id.syncButton)
        createEventButton = findViewById(R.id.createEventButton)
        contactUsButton = findViewById(R.id.contactUsButton)

        username = intent.getStringExtra("USERNAME") ?: ""
        isAdmin = intent.getBooleanExtra("IS_ADMIN", false)

        calendarTextView.text = getCalendarEvents()
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsRecyclerView.adapter = EventsAdapter(FakeDataSource.getEventsForUser(username)) { eventId ->
            startActivity(Intent(this, RegisterActivity::class.java).apply {
                putExtra("EVENT_ID", eventId)
                putExtra("USERNAME", username)
            })
        }

        if (isAdmin) {
            createEventButton.visibility = View.VISIBLE
            createEventButton.setOnClickListener {
                startActivity(Intent(this, CreateEventActivity::class.java))
            }
        } else {
            createEventButton.visibility = View.GONE
        }

        if (!isAdmin) {
            contactUsButton.visibility = View.VISIBLE
            contactUsButton.setOnClickListener { sendMessage() }
        } else {
            contactUsButton.visibility = View.GONE
        }

        syncButton.setOnClickListener {
            Toast.makeText(this, "Calendário sincronizado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCalendarEvents(): String {
        return FakeDataSource.getEventsForUser(username).joinToString("\n") {
            "${it.title}: ${it.dateTime.format(DateTimeFormatter.ISO_DATE)}"
        }
    }

    private fun sendMessage() {
        val message = "Olá, Sou $username e preciso de ajuda"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Enviar mensagem via")
        startActivity(shareIntent)
    }
}