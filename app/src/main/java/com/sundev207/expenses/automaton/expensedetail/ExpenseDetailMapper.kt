package com.sundev207.expenses.automaton.expensedetail

import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.automaton.expensedetail.ExpenseDetailInput.*
import com.sundev207.expenses.automaton.home.HomeInput.LoadExpensesInput
import com.sundev207.expenses.automaton.ApplicationOutput
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.database.DatabaseDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

typealias ExpenseDetailMapperResult = Pair<ExpenseDetailState, ApplicationOutput?>

class ExpenseDetailMapper(private val databaseDataSource: DatabaseDataSource) {

    fun map(state: ExpenseDetailState, input: ExpenseDetailInput): ExpenseDetailMapperResult {
        return when (input) {
            is SetExpenseInput -> setExpense(input)
            is ExpenseDetailInput.DeleteExpenseInput -> deleteExpense(state, input)
            is RestoreStateInput -> restoreState()
        }
    }

    private fun setExpense(input: SetExpenseInput) =
            ExpenseDetailMapperResult(ExpenseDetailState(input.expense), empty())

    private fun deleteExpense(
            state: ExpenseDetailState,
            input: DeleteExpenseInput
    ): ExpenseDetailMapperResult {
        val output = deleteExpenseFromDatabase(input.expense)
                .andThen(Observable.just(LoadExpensesInput as ApplicationInput))
        return ExpenseDetailMapperResult(state, output)
    }

    private fun deleteExpenseFromDatabase(expense: Expense): Completable {
        return databaseDataSource.deleteExpense(expense)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun restoreState() = ExpenseDetailMapperResult(ExpenseDetailState.INITIAL, empty())
}