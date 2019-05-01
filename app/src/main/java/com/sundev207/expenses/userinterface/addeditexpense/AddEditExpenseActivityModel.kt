package com.sundev207.expenses.userinterface.addeditexpense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sundev207.expenses.data.Tag

class AddEditExpenseActivityModel : ViewModel() {

    val selectedTags = MutableLiveData<List<Tag>>()

    fun selectTags(tags: List<Tag>) {
        selectedTags.value = tags
    }
}