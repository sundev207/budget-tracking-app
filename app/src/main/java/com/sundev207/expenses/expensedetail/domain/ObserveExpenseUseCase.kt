package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.database.DatabaseDataSource
import io.reactivex.Observable

class ObserveExpenseUseCase(private val databaseDataSource: DatabaseDataSource) {
    operator fun invoke(expenseId: Long): Observable<Expense> {
        return databaseDataSource.observeExpense(expenseId)
    }
}