package com.sundev207.expenses.ui.home

import android.content.Context
import com.sundev207.expenses.infrastructure.extensions.get
import com.sundev207.expenses.infrastructure.extensions.monthShortName
import com.sundev207.expenses.model.Expense
import java.util.*

class ExpenseItemModel(context: Context, val expense: Expense) : HomeItemModel {

    val month = expense.date.monthShortName(context)?.toUpperCase() ?: ""
    val day = "${expense.date.get(Calendar.DAY_OF_MONTH)}"
    val amount = "${"%.2f".format(expense.amount)} ${expense.currency.symbol}"
    val title = expense.title

    var click: (() -> Unit)? = null
}