package com.sundev207.expenses.ui.settings

import com.sundev207.expenses.data.Currency

class DefaultCurrencyItemModel(val currency: Currency) : SettingItemModel {

    val flag = currency.flag
    val subtitle = "${currency.title} (${currency.code})"

    var click: (() -> Unit)? = null
}