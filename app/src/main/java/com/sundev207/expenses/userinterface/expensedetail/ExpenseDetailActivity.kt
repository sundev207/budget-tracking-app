package com.sundev207.expenses.userinterface.expensedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sundev207.expenses.R
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.userinterface.common.BaseActivity

class ExpenseDetailActivity: BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ExpenseDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override var animationKind = ANIMATION_SLIDE_FROM_RIGHT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}