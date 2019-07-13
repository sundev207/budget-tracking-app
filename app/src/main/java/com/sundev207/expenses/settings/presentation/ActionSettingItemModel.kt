package com.sundev207.expenses.settings.presentation

open class ActionSettingItemModel(val title: String):
    SettingItemModel {

    var click: (() -> Unit)? = null
}