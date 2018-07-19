package com.sundev207.expenses.automaton.newexpense

import android.content.Context
import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.data.Currency
import com.sundev207.expenses.data.Date
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.Tag

abstract class NewExpenseInput: ApplicationInput() {

    data class LoadDefaultCurrencyInput(val context: Context) : NewExpenseInput()

    data class SetSelectedCurrencyInput(val selectedCurrency: Currency) : NewExpenseInput()

    data class SetSelectedDateInput(val selectedDate: Date) : NewExpenseInput()

    data class SetSelectedTagsInput(val selectedTags: List<Tag>) : NewExpenseInput()

    data class SetAmountInput(val amount: Float) : NewExpenseInput()

    data class SetTitleInput(val title: String) : NewExpenseInput()

    data class SetNotesInput(val notes: String) : NewExpenseInput()

    data class CreateExpenseInput(val expense: Expense) : NewExpenseInput()

    object RestoreStateInput : NewExpenseInput()
}