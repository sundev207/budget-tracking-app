package com.sundev207.expenses.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sundev207.expenses.R
import com.sundev207.expenses.model.Expense
import com.sundev207.expenses.ui.expensedetail.ExpenseDetailFragment
import com.sundev207.expenses.ui.home.HomeFragment
import com.sundev207.expenses.ui.newexpense.NewExpenseFragment
import com.sundev207.expenses.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        setupToolbar()
        navigateToHome()
    }

    private fun bindWidgets() {
        toolbar = findViewById(R.id.toolbar)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    fun navigateToHome() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container,
                        HomeFragment.newInstance())
                .addToBackStack("HomeFragment")
                .commit()
    }

    fun navigateToSettings() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container, SettingsFragment.newInstance())
                .addToBackStack("SettingsFragment")
                .commit()
    }

    fun navigateToNewExpense() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container, NewExpenseFragment.newInstance())
                .addToBackStack("NewExpenseFragment")
                .commit()
    }

    fun navigateToExpenseDetail(expense: Expense) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container, ExpenseDetailFragment.newInstance(expense))
                .addToBackStack("ExpenseDetailFragment")
                .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    // override fun onSupportNavigateUp() = findNavController(R.id.fragment_navigation_host).navigateUp()
}
