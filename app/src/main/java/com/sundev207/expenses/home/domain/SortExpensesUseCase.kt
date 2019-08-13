package com.sundev207.expenses.home.domain

import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.util.extensions.toEpochMillis

class SortExpensesUseCase {

    operator fun invoke(expenses: List<Expense>): List<Expense> {
        return expenses.sortedByDescending { it.date.toEpochMillis() }
    }
}