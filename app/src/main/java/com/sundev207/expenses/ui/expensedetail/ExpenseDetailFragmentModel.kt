package com.sundev207.expenses.ui.expensedetail

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.R
import com.sundev207.expenses.infrastructure.extensions.toString
import com.sundev207.expenses.infrastructure.utils.Event
import com.sundev207.expenses.infrastructure.utils.runOnBackground
import com.sundev207.expenses.model.ApplicationDatabase
import com.sundev207.expenses.model.Expense

class ExpenseDetailFragmentModel(
        application: Application,
        private val expense: Expense
) : AndroidViewModel(application) {

    val amount = "${"%.2f".format(expense.amount)} ${expense.currency.symbol}"
    val currency = "(${expense.currency.title} • ${expense.currency.code})"
    val title = expense.title
    val user = expense.userName
    val date: String
    val notes: String

    val finish = Event()

    private val database = application.database

    init {
        date = createDate(expense)
        notes = createNotes(application, expense)
    }

    private fun createDate(expense: Expense): String {
        return expense.date.toString("dd-MM-yyyy")
    }

    private fun createNotes(context: Context, expense: Expense): String {
        val notes = expense.notes
        return if (notes.isNotEmpty()) notes
        else context.getString(R.string.ui_expense_detail_no_notes)
    }

    fun delete() {
        runOnBackground { database.expenseDao().delete(expense) }
        finish.next()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
            private val application: Application,
            private val expense: Expense
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ExpenseDetailFragmentModel(application, expense) as T
        }
    }
}