package com.example.extensao

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.threeten.bp.ZoneId
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {
    private lateinit var eventTitle: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventDescription: TextView
    private lateinit var registerButton: Button

    private lateinit var event: Event
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        eventTitle = findViewById(R.id.eventTitle)
        eventDate = findViewById(R.id.eventDate)
        eventDescription = findViewById(R.id.eventDescription)
        registerButton = findViewById(R.id.registerButton)

        val eventId = intent.getIntExtra("EVENT_ID", -1)
        username = intent.getStringExtra("USERNAME") ?: ""
        event = FakeDataSource.getAllEvents().first { it.id == eventId }

        eventTitle.text = event.title
        eventDate.text = event.dateTime.toString()
        eventDescription.text = event.description

        registerButton.setOnClickListener {
            FakeDataSource.enrollUser(event.id, username)
            addToCalendar()
            finish()
        }
    }

    private fun addToCalendar() {
        val startMillis = event.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = startMillis + TimeUnit.HOURS.toMillis(1)

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, event.title)
            .putExtra(CalendarContract.Events.DESCRIPTION, event.description)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Virtual")
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)

        startActivity(intent)
    }
}