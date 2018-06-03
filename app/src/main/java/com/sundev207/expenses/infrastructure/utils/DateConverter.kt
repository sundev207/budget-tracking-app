package com.sundev207.expenses.infrastructure.utils

import androidx.room.TypeConverter
import com.sundev207.expenses.data.Date

class DateConverter {

    companion object {

        @JvmStatic
        @TypeConverter
        fun toDate(long: Long) = Date(long)

        @JvmStatic
        @TypeConverter
        fun toLong(date: Date) = date.utcTimestamp
    }
}