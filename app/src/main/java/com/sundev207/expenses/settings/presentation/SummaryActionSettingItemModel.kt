package com.sundev207.expenses.settings.presentation

class SummaryActionSettingItemModel(val title: String, val summary: String) : SettingItemModel {
    var click: (() -> Unit)? = null
}