package com.sundev207.expenses.automaton.expensedetail

import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.data.Expense

interface ExpenseDetailInputs {

    data class SetExpenseInput(val expense: Expense?) : ApplicationInput

    data class DeleteExpenseInput(val expense: Expense) : ApplicationInput

    object RestoreStateInput : ApplicationInput
}