package com.sundev207.expenses.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

@Parcelize
data class Expense(
    val id: String,
    val amount: Double,
    val currency: Currency,
    val title: String,
    val tags: List<Tag>,
    val date: LocalDate,
    val notes: String,
    val timestamp: Long?
) : Parcelable