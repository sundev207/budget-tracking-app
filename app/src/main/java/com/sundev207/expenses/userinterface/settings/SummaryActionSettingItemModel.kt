package com.sundev207.expenses.userinterface.settings

class SummaryActionSettingItemModel(val title: String, val summary: String) : SettingItemModel {

    var click: (() -> Unit)? = null
}