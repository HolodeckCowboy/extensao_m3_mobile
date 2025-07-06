package com.example.extensao

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class EventApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}