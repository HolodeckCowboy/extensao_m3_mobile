package com.example.extensao

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.threeten.bp.LocalDateTime

class CreateEventActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        createButton = findViewById(R.id.createButton)

        createButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val dateTime = LocalDateTime.now().plusDays(7)

            if (title.isNotBlank()) {
                val newEvent = Event(
                    id = 0,
                    title = title,
                    description = description,
                    dateTime = dateTime
                )
                FakeDataSource.addEvent(newEvent)
                sendNotification(title, dateTime.toString())
                finish()
            } else {
                Toast.makeText(this, "Título é obrigatório", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendNotification(title: String, date: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Generate unique ID
        val notificationId = System.currentTimeMillis().toInt()

        // Build notification
        val builder = NotificationCompat.Builder(this, "event_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Novo evento: $title")
            .setContentText("Data: $date")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Novo evento '$title' foi agendado para a data $date. Clique para mais detalhes"))

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel",
                "Novos eventos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Novo evento"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}