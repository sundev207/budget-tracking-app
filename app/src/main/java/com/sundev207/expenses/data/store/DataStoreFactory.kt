package com.sundev207.expenses.data.store

import com.sundev207.expenses.Application
import com.sundev207.expenses.authentication.AuthenticationManager
import com.sundev207.expenses.data.firebase.FirebaseDataStore
import com.sundev207.expenses.data.room.RoomDataStore

object DataStoreFactory {

    fun get(application: Application): DataStore {
        val shouldUseFirebase = AuthenticationManager.getInstance(application).isUserSignedIn()

        return if (shouldUseFirebase ) {
            FirebaseDataStore.getInstance(application)
        } else {
            RoomDataStore.getInstance(application)
        }
    }
}