package com.sundev207.expenses.home.presentation

import android.os.Bundle
import com.sundev207.expenses.R
import com.sundev207.expenses.common.presentation.BaseActivity

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}