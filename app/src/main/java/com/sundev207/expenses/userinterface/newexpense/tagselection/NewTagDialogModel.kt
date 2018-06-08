package com.sundev207.expenses.userinterface.newexpense.tagselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.data.Tag
import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.infrastructure.utils.Variable

class NewTagDialogModel(private val databaseDataSource: DatabaseDataSource) : ViewModel() {

    val isAddEnabled = Variable(false)

    private var name = ""

    fun updateName(name: String) {
        this.name = name
        isAddEnabled.value = name.isNotEmpty()
    }

    fun addTag() {
        val tag = Tag(0, name)
        databaseDataSource.insertTag(tag)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val databaseDataSource = DatabaseDataSource(application.database)
            return NewTagDialogModel(databaseDataSource) as T
        }
    }
}