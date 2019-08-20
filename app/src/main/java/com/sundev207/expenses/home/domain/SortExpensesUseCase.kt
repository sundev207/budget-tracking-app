package com.sundev207.expenses.home.domain

import com.sundev207.expenses.data.Expense

class SortExpensesUseCase {

    private val comparator by lazy {
        compareByDescending(Expense::date).thenByDescending(Expense::createdAt)
    }

    operator fun invoke(expenses: List<Expense>) = expenses.sortedWith(comparator)
}