package com.sundev207.expenses.ui.home

import com.sundev207.expenses.model.Currency

class CurrencySummaryItemModel(currency: Currency, amount: Float) {

    val amount = "${"%.2f".format(amount)} ${currency.symbol}"
    val currency = "(${currency.title} • ${currency.code})"
}