package com.sundev207.expenses.source

import android.content.Context
import android.content.SharedPreferences
import com.sundev207.expenses.R
import com.sundev207.expenses.infrastructure.utils.CurrencyConverter
import com.sundev207.expenses.data.Currency

class PreferenceDataSource {

    fun getDefaultCurrency(context: Context): Currency {
        val preferences = getPreferences(context)
        val key = context.getString(R.string.key_default_currency)
        val currencyIfNotSet = Currency.USD
        val defaultCurrencyString = preferences.getString(key,
                CurrencyConverter.toString(currencyIfNotSet))
        return CurrencyConverter.toCurrency(defaultCurrencyString) ?: currencyIfNotSet
    }

    fun setDefaultCurrency(context: Context, currency: Currency) {
        val preferences = getPreferences(context)
        val key = context.getString(R.string.key_default_currency)
        val value = CurrencyConverter.toString(currency)
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getPreferences(context: Context): SharedPreferences {
        val preferencesKey = context.getString(R.string.key_preferences)
        return context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
    }
}