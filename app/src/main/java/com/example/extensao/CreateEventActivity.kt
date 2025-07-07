package com.example.extensao

import android.Manifest
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {

    private val CHANNEL_ID = "event_channel"
    private var selectedDateForStorage: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        createNotificationChannel()

        val nameInput = findViewById<EditText>(R.id.editTextEventName)
        val descriptionInput = findViewById<EditText>(R.id.editTextEventDescription)
        val createButton = findViewById<Button>(R.id.buttonCreateEvent)
        val selectDateButton = findViewById<TextView>(R.id.textViewSelectDate)
        val selectTimeButton = findViewById<TextView>(R.id.textViewSelectTime)

        selectDateButton.setOnClickListener {
            showDatePickerDialog(selectDateButton)
        }

        selectTimeButton.setOnClickListener {
            showTimePickerDialog(selectTimeButton)
        }

        createButton.setOnClickListener {
            val name = nameInput.text.toString()
            val description = descriptionInput.text.toString()

            if (name.isNotBlank() && description.isNotBlank() && selectedDateForStorage.isNotEmpty() && selectedTime.isNotEmpty()) {
                val newEvent = Event(
                    id = (EventManager.getAllEvents().size + 1),
                    name = name,
                    date = selectedDateForStorage,
                    time = selectedTime,
                    description = description
                )
                EventManager.addEvent(newEvent)

                sendNotification(newEvent)

                Toast.makeText(this, getString(R.string.create_success), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.create_fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(dateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val calendarSelected = Calendar.getInstance()
            calendarSelected.set(selectedYear, selectedMonth, selectedDay)

            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateForDisplay = displayFormat.format(calendarSelected.time)
            dateTextView.text = dateForDisplay

            val storageFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDateForStorage = storageFormat.format(calendarSelected.time)

        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(timeTextView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            timeTextView.text = selectedTime
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(event: Event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text_format, event.name, DateFormatter.formatToDisplay(event.date)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(event.id, builder.build())
        }
    }
}