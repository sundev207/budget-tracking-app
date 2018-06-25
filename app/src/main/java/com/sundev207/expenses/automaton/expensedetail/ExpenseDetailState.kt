package com.sundev207.expenses.automaton.expensedetail

import com.sundev207.expenses.data.Expense

data class ExpenseDetailState(val expense: Expense?) {
    companion object { val INITIAL = ExpenseDetailState(
            null)
    }
}