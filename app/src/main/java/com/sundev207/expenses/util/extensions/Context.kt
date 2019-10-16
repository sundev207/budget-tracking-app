package com.sundev207.expenses.util.extensions

import android.content.Context
import com.sundev207.expenses.Application

val Context.application: Application
    get() = applicationContext as Application