package com.sundev207.expenses.userinterface.newexpense.tagselection

import com.sundev207.expenses.data.Tag

class TagItemModel(val tag: Tag): TagSelectionItemModel {

    var isChecked = false
    val name = tag.name

    var checkClick: (() -> Unit)? = null
    var deleteClick: (() -> Unit)? = null
}