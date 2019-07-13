package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.database.DatabaseDataSource
import io.reactivex.Completable

class DeleteExpenseUseCase(private val databaseDataSource: DatabaseDataSource) {
    
    operator fun invoke(expense: Expense): Completable {
        return databaseDataSource.deleteExpense(expense)
    }
}