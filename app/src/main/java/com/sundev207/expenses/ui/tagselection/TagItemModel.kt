package com.sundev207.expenses.ui.tagselection

import com.sundev207.expenses.data.Tag

class TagItemModel(val tag: Tag): TagSelectionItemModel {

    var isSelected = false
    val name = tag.name

    var selectClick: (() -> Unit)? = null
    var removeClick: (() -> Unit)? = null
}