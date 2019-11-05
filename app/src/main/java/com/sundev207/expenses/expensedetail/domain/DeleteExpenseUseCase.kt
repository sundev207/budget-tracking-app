package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.model.Expense
import com.sundev207.expenses.data.store.DataStore
import io.reactivex.Completable

class DeleteExpenseUseCase(private val dataStore: DataStore) {

    operator fun invoke(expense: Expense): Completable {
        return dataStore.deleteExpense(expense)
    }
}