package com.sundev207.expenses

import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sundev207.expenses.automaton.ApplicationAutomaton
import com.sundev207.expenses.data.database.ApplicationDatabase
import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.data.preference.PreferenceDataSource

private const val DATABASE_NAME = "database"

class Application : android.app.Application() {

    lateinit var database: ApplicationDatabase
    lateinit var automaton: ApplicationAutomaton

    override fun onCreate() {
        super.onCreate()
        initializeThreeTeen()
        initializeDatabase()
        initializeAutomaton()
        startAutomaton()
    }

    private fun initializeThreeTeen() {
        AndroidThreeTen.init(this)
    }

    private fun initializeDatabase() {
        database = Room.databaseBuilder(this, ApplicationDatabase::class.java, DATABASE_NAME).build()
    }

    private fun initializeAutomaton() {
        val databaseDataSource = DatabaseDataSource(database)
        val preferenceDataSource = PreferenceDataSource()
        automaton = ApplicationAutomaton(databaseDataSource, preferenceDataSource)
    }

    private fun startAutomaton() = automaton.start()
}
