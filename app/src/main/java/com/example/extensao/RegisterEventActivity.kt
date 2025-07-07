package com.example.extensao

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RegisterEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_event)

        val eventId = intent.getIntExtra("EVENT_ID", -1)
        val event = EventManager.getEventById(eventId)

        if (event == null) {
            Toast.makeText(this, getString(R.string.register_event_not_found), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.textViewEventNameDetail).text = event.name

        val formattedDate = DateFormatter.formatToDisplay(event.date)
        findViewById<TextView>(R.id.textViewEventDateTimeDetail).text = getString(R.string.register_date_time_format, formattedDate, event.time)
        findViewById<TextView>(R.id.textViewEventDescriptionDetail).text = event.description

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            event.isUserEnrolled = true

            val calIntent = Intent(Intent.ACTION_INSERT)
            calIntent.data = CalendarContract.Events.CONTENT_URI
            calIntent.putExtra(CalendarContract.Events.TITLE, event.name)
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description)

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val dateTime = "${event.date} ${event.time}"
            val startTime = sdf.parse(dateTime)?.time

            if(startTime != null) {
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 3600000) // 1 hour duration
            }

            if (calIntent.resolveActivity(packageManager) != null) {
                startActivity(calIntent)
                Toast.makeText(this, getString(R.string.register_success_message, event.name), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.main_no_calendar_app), Toast.LENGTH_SHORT).show()
            }
        }
    }
}