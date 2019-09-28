package com.sundev207.expenses

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sundev207.expenses.data.database.ApplicationDatabase

class Application : android.app.Application() {

    val database by lazy { ApplicationDatabase.build(this) }

    val firestore by lazy {
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()
        FirebaseFirestore.getInstance().apply { firestoreSettings = settings }
    }

    override fun onCreate() {
        super.onCreate()
        initializeThreeTeen()
    }

    private fun initializeThreeTeen() {
        AndroidThreeTen.init(this)
    }
}
