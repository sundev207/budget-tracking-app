package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.firebase.FirestoreDataSource
import com.sundev207.expenses.data.model.Expense
import io.reactivex.Observable

class ObserveExpenseUseCase(private val firestoreDataSource: FirestoreDataSource) {
    operator fun invoke(expenseId: String): Observable<Expense> {
        return firestoreDataSource.observeExpense(expenseId)
    }
}