package com.sundev207.expenses.ui.home

class TagFilterItemModel(val tagFilter: TagFilter): HomeItemModel {

    val chips = tagFilter.tags.map { it.name }
    var clearClick: (() -> Unit)? = null
}