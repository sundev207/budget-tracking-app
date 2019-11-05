package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.model.Expense
import com.sundev207.expenses.data.store.DataStore
import io.reactivex.Observable

class ObserveExpenseUseCase(private val dataStore: DataStore) {
    operator fun invoke(expenseId: String): Observable<Expense> {
        return dataStore.observeExpense(expenseId)
    }
}