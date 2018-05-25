package com.sundev207.expenses.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.infrastructure.utils.Variable
import com.sundev207.expenses.infrastructure.utils.runOnBackground
import com.sundev207.expenses.model.ApplicationDatabase
import com.sundev207.expenses.model.User

class NewUserDialogModel(private val database: ApplicationDatabase) : ViewModel() {

    var name: String? = null
    val isAddEnabled = Variable(false)

    fun updateName(name: String?) {
        this.name = name
        isAddEnabled.value = name != null && name.isNotEmpty()
    }

    fun addUser() {
        val user = User(name ?: "No name")
        runOnBackground { database.userDao().insert(user) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NewUserDialogModel(application.database) as T
        }
    }
}