package com.sundev207.expenses.settings.domain

import com.sundev207.expenses.data.database.DatabaseDataSource
import io.reactivex.Completable

class DeleteAllExpensesUseCase(private val databaseDataSource: DatabaseDataSource) {

    operator fun invoke(): Completable {
        return databaseDataSource.deleteAllExpenses()
    }
}