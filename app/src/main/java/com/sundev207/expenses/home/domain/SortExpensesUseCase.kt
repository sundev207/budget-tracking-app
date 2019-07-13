package com.sundev207.expenses.home.domain

import com.sundev207.expenses.data.Expense

class SortExpensesUseCase {

    operator fun invoke(expenses: List<Expense>): List<Expense> {
        return expenses.sortedByDescending { it.date.utcTimestamp }
    }
}