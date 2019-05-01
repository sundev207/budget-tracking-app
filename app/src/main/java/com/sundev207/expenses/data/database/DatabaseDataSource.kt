package com.sundev207.expenses.data.database

import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.ExpenseTagJoin
import com.sundev207.expenses.data.Tag
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class DatabaseDataSource(private val database: ApplicationDatabase) {

    // Expenses

    fun getExpenses(): Observable<List<Expense>> {
        return Observable.defer { Observable.just(database.expenseDao().getAll()) }
                .subscribeOn(Schedulers.io())
                .map { it.map { expense -> addTagsToExpense(expense) } }
    }

    private fun addTagsToExpense(expense: Expense): Expense {
        expense.tags = getExpenseTags(expense)
        return expense
    }

    private fun getExpenseTags(expense: Expense): List<Tag> {
        return database.expenseTagJoinDao().getTagsWithExpenseId(expense.id)
    }

    fun insertExpense(expense: Expense): Observable<Long> {
        return Observable.defer {
            val id = database.expenseDao().insert(expense)

            for (tag in expense.tags) {
                val join = ExpenseTagJoin(id, tag.id)
                database.expenseTagJoinDao().insert(join)
            }

            Observable.just(id)
        }
    }

    fun updateExpense(expense: Expense): Completable {
        return Completable.fromAction {
            database.expenseTagJoinDao().deleteByExpenseId(expense.id)

            database.expenseDao().update(expense)

            for (tag in expense.tags) {
                val join = ExpenseTagJoin(expense.id, tag.id)
                database.expenseTagJoinDao().insert(join)
            }
        }
    }

    fun deleteExpense(expense: Expense): Completable {
        return Completable.defer { database.expenseDao().delete(expense); Completable.complete() }
    }

    fun deleteAllExpenses(): Completable {
        return Completable.defer { database.expenseDao().deleteAll(); Completable.complete() }
    }

    // Tags

    fun observeTags(): Observable<List<Tag>> {
        return Observable.fromCallable { database.tagDao().getAll() }
    }

    fun insertTag(tag: Tag): Single<Long> {
        return Single.fromCallable { database.tagDao().insert(tag) }
    }

    fun deleteTag(tag: Tag): Completable {
        return Completable.fromAction { database.tagDao().delete(tag) }
    }
}