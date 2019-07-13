package com.sundev207.expenses.home.domain

import com.sundev207.expenses.data.Tag

class SortTagsUseCase {
    operator fun invoke(tags: List<Tag>): List<Tag> {
        return tags.sortedBy { it.name }
    }
}