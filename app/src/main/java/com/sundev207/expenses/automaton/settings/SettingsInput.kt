package com.sundev207.expenses.automaton.settings

import android.content.Context
import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.automaton.settings.SettingsState.ExpenseExportState
import com.sundev207.expenses.data.Currency

sealed class SettingsInput: ApplicationInput() {

    data class LoadDefaultCurrencyInput(val context: Context) : SettingsInput()

    data class SetDefaultCurrencyInput(val defaultCurrency: Currency?) : SettingsInput()

    data class SaveDefaultCurrencyInput(
            val context: Context,
            val defaultCurrency: Currency
    ) : SettingsInput()

    data class ExportExpensesInput(val context: Context) : SettingsInput()

    data class SetExpenseExportStateInput(
            val expenseExportState: ExpenseExportState
    ) : SettingsInput()

    object DeleteAllExpensesInput : SettingsInput()

    object RestoreStateInput : SettingsInput()
}