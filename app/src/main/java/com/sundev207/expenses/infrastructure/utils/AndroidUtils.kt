package com.sundev207.expenses.infrastructure.utils

import android.os.AsyncTask

fun runOnBackground(function: () -> Unit) {
    AsyncTask.execute({ function() })
}