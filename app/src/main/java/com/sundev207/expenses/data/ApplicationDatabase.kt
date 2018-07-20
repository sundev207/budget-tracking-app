package com.sundev207.expenses.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.ExpenseTagJoin
import com.sundev207.expenses.data.Tag
import com.sundev207.expenses.data.dao.ExpenseDao
import com.sundev207.expenses.data.dao.ExpenseTagJoinDao
import com.sundev207.expenses.data.dao.TagDao
import com.sundev207.expenses.infrastructure.utils.CurrencyConverter
import com.sundev207.expenses.infrastructure.utils.DateConverter

@Database(
        entities = [
            Expense::class,
            Tag::class,
            ExpenseTagJoin::class
        ],
        version = 1,
        exportSchema = false)
@TypeConverters(
        value = [
            CurrencyConverter::class,
            DateConverter::class
        ]
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun tagDao(): TagDao

    abstract fun expenseTagJoinDao(): ExpenseTagJoinDao
}