package com.sundev207.expenses.userinterface.settings

open class ActionSettingItemModel(val title: String): SettingItemModel {

    var click: (() -> Unit)? = null
}