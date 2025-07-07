package com.example.extensao

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userType = intent.getStringExtra("USER_TYPE") ?: "USER"

        setupUI()
        setupEventListeners()
    }

    private fun setupUI() {
        val fabContactUs = findViewById<FloatingActionButton>(R.id.fabContactUs)
        val buttonEdit = findViewById<ImageButton>(R.id.buttonEdit)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewEvents)

        if (userType == "ADMIN") {
            buttonEdit.visibility = View.VISIBLE
            fabContactUs.visibility = View.GONE
            recyclerView.adapter = EventAdapter(EventManager.getAllEvents()) { event ->
                val intent = Intent(this, RegisterEventActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)
                startActivity(intent)
            }
        } else {
            buttonEdit.visibility = View.GONE
            fabContactUs.visibility = View.VISIBLE
            recyclerView.adapter = EventAdapter(EventManager.getAllEvents()) { event ->
                val intent = Intent(this, RegisterEventActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)
                startActivity(intent)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEventListeners() {
        findViewById<ImageButton>(R.id.buttonEdit).setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }

        findViewById<FloatingActionButton>(R.id.fabContactUs).setOnClickListener {
            val message = getString(R.string.main_contact_us_message)
            val phoneNumber = "12345678910" // Format for SMS intent
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", message)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.main_no_messaging_app), Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.buttonSync).setOnClickListener {
            syncCalendar()
        }
    }

    private fun syncCalendar() {
        val eventsToSync = if(userType == "ADMIN") EventManager.getAllEvents() else EventManager.getEnrolledEvents()

        if (eventsToSync.isEmpty()){
            Toast.makeText(this, getString(R.string.main_no_events_to_sync), Toast.LENGTH_SHORT).show()
            return
        }

        for (event in eventsToSync) {
            val calIntent = Intent(Intent.ACTION_INSERT)
            calIntent.data = CalendarContract.Events.CONTENT_URI
            calIntent.putExtra(CalendarContract.Events.TITLE, event.name)
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description)

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val dateTime = "${event.date} ${event.time}"
            val startTime = sdf.parse(dateTime)?.time ?: continue

            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 3600000)

            if (calIntent.resolveActivity(packageManager) != null) {
                startActivity(calIntent)
            } else {
                Toast.makeText(this, getString(R.string.main_no_calendar_app), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (findViewById<RecyclerView>(R.id.recyclerViewEvents).adapter)?.notifyDataSetChanged()
    }
}