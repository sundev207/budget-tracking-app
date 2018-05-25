package com.sundev207.expenses.ui.settings

import com.sundev207.expenses.model.User

class UserItemModel(val user: User): SettingItemModel {

    val name = user.name
    var deleteButtonClick: (() -> Unit)? = null
}