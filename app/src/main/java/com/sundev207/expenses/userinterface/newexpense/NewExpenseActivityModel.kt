package com.sundev207.expenses.userinterface.newexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.data.Tag
import com.sundev207.expenses.infrastructure.utils.Variable

class NewExpenseActivityModel: ViewModel() {

    val selectedTags = Variable(emptyList<Tag>())

    fun selectTags(tags: List<Tag>) {
        selectedTags.value = tags
    }
}