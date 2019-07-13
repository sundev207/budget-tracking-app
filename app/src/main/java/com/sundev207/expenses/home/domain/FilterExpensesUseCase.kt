package com.sundev207.expenses.home.domain

import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.home.presentation.DateRange
import com.sundev207.expenses.home.presentation.TagFilter

class FilterExpensesUseCase {

    operator fun invoke(
        expenses: List<Expense>,
        dateRange: DateRange,
        tagFilter: TagFilter?
    ): List<Expense> {
        return expenses.filter { expense ->
            dateRange.contains(expense.date) &&
                    tagFilter?.let { expense.tags.containsAll(it.tags) } ?: true
        }
    }
}