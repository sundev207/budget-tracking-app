package com.sundev207.expenses.infrastructure.extensions

import androidx.core.os.LocaleListCompat
import java.util.*

fun LocaleListCompat.default(): Locale? = get(0)