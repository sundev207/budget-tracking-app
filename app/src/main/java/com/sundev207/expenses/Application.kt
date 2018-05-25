package com.sundev207.expenses

import androidx.room.Room
import com.sundev207.expenses.model.ApplicationDatabase

class Application : android.app.Application() {

    lateinit var database: ApplicationDatabase

    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }

    private fun initializeDatabase() {
        database = Room.databaseBuilder(this, ApplicationDatabase::class.java, "database").build()
    }
}
