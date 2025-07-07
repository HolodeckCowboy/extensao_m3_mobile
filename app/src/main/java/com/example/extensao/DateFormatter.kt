package com.example.extensao

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {

    private val modelFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatToDisplay(dateString: String): String {
        return try {
            val date = modelFormat.parse(dateString)
            if (date != null) {
                displayFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }
}