package com.example.extensao

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.extensao.EventManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize event data with context for localization
        EventManager.initializeEvents(applicationContext)

        val usernameInput = findViewById<EditText>(R.id.editTextUsername)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            when {
                username == "user" && password == "user" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_TYPE", "USER")
                    startActivity(intent)
                    finish()
                }
                username == "admin" && password == "admin" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_TYPE", "ADMIN")
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, getString(R.string.login_invalid_credentials), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}