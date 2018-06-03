package com.sundev207.expenses.ui.expensedetail

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.R
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.infrastructure.utils.Event

class ExpenseDetailFragmentModel(
        application: Application,
        private val expense: Expense,
        private val databaseDataSource: DatabaseDataSource
) : AndroidViewModel(application) {

    val amount = "${"%.2f".format(expense.amount)} ${expense.currency.symbol}"
    val currency = "(${expense.currency.title} • ${expense.currency.code})"
    val title = expense.title
    val tags = expense.tags
    val date: String
    val notes: String

    val finish = Event()

    init {
        date = createDate(expense)
        notes = createNotes(application, expense)
    }

    private fun createDate(expense: Expense) = expense.date.toReadableString()

    private fun createNotes(context: Context, expense: Expense): String {
        val notes = expense.notes
        return if (notes.isNotEmpty()) notes
        else context.getString(R.string.no_notes)
    }

    fun delete() {
        databaseDataSource.deleteExpense(expense)
        finish.next()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
            private val application: Application,
            private val expense: Expense
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val databaseDataSource = DatabaseDataSource(application.database)
            return ExpenseDetailFragmentModel(application, expense, databaseDataSource) as T
        }
    }
}