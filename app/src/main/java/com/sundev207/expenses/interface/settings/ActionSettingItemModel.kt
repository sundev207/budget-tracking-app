package com.sundev207.expenses.`interface`.settings

open class ActionSettingItemModel(val title: String): SettingItemModel {

    var click: (() -> Unit)? = null
}