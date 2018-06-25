package com.sundev207.expenses.automaton.newexpense

import com.sundev207.expenses.automaton.newexpense.tagselection.TagSelectionState
import com.sundev207.expenses.data.Currency
import com.sundev207.expenses.data.Date
import com.sundev207.expenses.data.Tag

data class NewExpenseState(
        val selectedCurrency: Currency,
        val selectedDate: Date,
        val selectedTags: List<Tag>,
        val amount: Float,
        val title: String,
        val notes: String,
        val tagSelectionState: TagSelectionState
) {
    companion object {
        val INITIAL = NewExpenseState(Currency.USD,
                Date.now(),
                ArrayList(),
                0f,
                "",
                "",
                TagSelectionState.INITIAL)
    }
}