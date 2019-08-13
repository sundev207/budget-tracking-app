package com.sundev207.expenses.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.ExpenseTagJoin
import com.sundev207.expenses.data.Tag
import com.sundev207.expenses.data.database.converter.CurrencyConverter
import com.sundev207.expenses.data.database.converter.LocalDateConverter
import com.sundev207.expenses.data.database.dao.ExpenseDao
import com.sundev207.expenses.data.database.dao.ExpenseTagJoinDao
import com.sundev207.expenses.data.database.dao.TagDao

@Database(
    entities = [
        Expense::class,
        Tag::class,
        ExpenseTagJoin::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    value = [
        CurrencyConverter::class,
        LocalDateConverter::class
    ]
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun tagDao(): TagDao

    abstract fun expenseTagJoinDao(): ExpenseTagJoinDao
}