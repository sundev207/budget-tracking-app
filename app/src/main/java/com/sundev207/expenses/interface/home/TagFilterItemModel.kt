package com.sundev207.expenses.`interface`.home

class TagFilterItemModel(val tagFilter: TagFilter): HomeItemModel {

    val chips = tagFilter.tags.map { it.name }
    var clearClick: (() -> Unit)? = null
}