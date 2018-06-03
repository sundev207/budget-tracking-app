package com.sundev207.expenses.infrastructure.utils

import androidx.room.TypeConverter
import com.sundev207.expenses.data.Currency

class CurrencyConverter {

    companion object {

        @JvmStatic
        @TypeConverter
        fun toCurrency(string: String) = Currency.fromCode(string)

        @JvmStatic
        @TypeConverter
        fun toString(currency: Currency) = currency.toString()
    }
}