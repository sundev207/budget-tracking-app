package com.sundev207.expenses.automaton.expensedetail

import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.data.Expense

sealed class ExpenseDetailInput: ApplicationInput() {

    data class SetExpenseInput(val expense: Expense?) : ExpenseDetailInput()

    data class DeleteExpenseInput(val expense: Expense) : ExpenseDetailInput()

    object RestoreStateInput : ExpenseDetailInput()
}