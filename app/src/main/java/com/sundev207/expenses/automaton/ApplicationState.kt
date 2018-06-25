package com.sundev207.expenses.automaton

import com.sundev207.expenses.automaton.expensedetail.ExpenseDetailState
import com.sundev207.expenses.automaton.home.HomeState
import com.sundev207.expenses.automaton.newexpense.NewExpenseState
import com.sundev207.expenses.automaton.settings.SettingsState

data class ApplicationState(
        val expenseDetailState: ExpenseDetailState,
        val homeState: HomeState,
        val newExpenseState: NewExpenseState,
        val settingsState: SettingsState
) {
    companion object {
        val INITIAL = ApplicationState(ExpenseDetailState.INITIAL,
                HomeState.INITIAL,
                NewExpenseState.INITIAL,
                SettingsState.INITIAL)
    }
}