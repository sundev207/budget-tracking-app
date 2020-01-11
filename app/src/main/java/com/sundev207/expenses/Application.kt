package com.sundev207.expenses

import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sundev207.expenses.data.preference.PreferenceDataSource
import com.sundev207.expenses.data.room.ApplicationDatabase

class Application : android.app.Application() {

    val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    val database by lazy { ApplicationDatabase.build(this) }

    val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate() {
        super.onCreate()
        initializeThreeTeen()
        applyNightMode()
    }

    private fun initializeThreeTeen() {
        AndroidThreeTen.init(this)
    }

    private fun applyNightMode() {
        val darkMode = PreferenceDataSource().getTheme(applicationContext)
        AppCompatDelegate.setDefaultNightMode(darkMode.toNightMode())
    }
}
