package com.sundev207.expenses.automaton.newexpense.tagselection

import com.sundev207.expenses.data.Tag

data class TagSelectionState(
        val tags: List<Tag>,
        val checkedTags: HashSet<Tag>
) {
    companion object {
        val INITIAL = TagSelectionState(ArrayList(), HashSet())
    }
}