package com.sundev207.expenses.automaton.newexpense.tagselection

import com.sundev207.expenses.automaton.ApplicationInput
import com.sundev207.expenses.data.Tag

interface TagSelectionInputs {

    object LoadTagsInput : ApplicationInput

    data class SetTagsInput(val tags: List<Tag>) : ApplicationInput

    data class CreateTagInput(val tag: Tag) : ApplicationInput

    data class DeleteTagInput(val tag: Tag) : ApplicationInput

    data class CheckTagInput(val tag: Tag) : ApplicationInput

    data class UncheckTagInput(val tag: Tag) : ApplicationInput

    object RestoreStateInput : ApplicationInput
}