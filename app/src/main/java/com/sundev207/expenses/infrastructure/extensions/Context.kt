package com.sundev207.expenses.infrastructure.extensions

import android.content.Context
import com.sundev207.expenses.Application

val Context.application: Application
    get() = applicationContext as Application