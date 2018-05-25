package com.sundev207.expenses.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sundev207.expenses.infrastructure.utils.CurrencyConverter
import com.sundev207.expenses.infrastructure.utils.DateConverter

@Database(entities = [Expense::class, User::class], version = 1, exportSchema = false)
@TypeConverters(value = [CurrencyConverter::class, DateConverter::class])
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun userDao(): UserDao
}