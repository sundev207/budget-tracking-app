package com.sundev207.expenses.expensedetail.domain

import com.sundev207.expenses.data.firebase.FirestoreDataSource
import com.sundev207.expenses.data.model.Expense
import io.reactivex.Completable

class DeleteExpenseUseCase(private val firestoreDataSource: FirestoreDataSource) {

    operator fun invoke(expense: Expense): Completable {
        return firestoreDataSource.deleteExpense(expense)
    }
}