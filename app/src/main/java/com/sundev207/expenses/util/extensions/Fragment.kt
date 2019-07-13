package com.sundev207.expenses.util.extensions

import android.view.View
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard(view: View) {
    requireActivity().showKeyboard(view)
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}