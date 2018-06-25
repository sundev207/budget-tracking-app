package com.sundev207.expenses.automaton.settings

import com.sundev207.expenses.data.Currency

data class SettingsState(
        val defaultCurrency: Currency?,
        val expenseExportState: ExpenseExportState
) {

    sealed class ExpenseExportState {

        companion object { val INITIAL = None }

        object None : ExpenseExportState()
        object Working : ExpenseExportState()
        data class Finished(val isSuccessful: Boolean) : ExpenseExportState()
    }

    companion object {
        val INITIAL = SettingsState(null, ExpenseExportState.INITIAL)
    }
}