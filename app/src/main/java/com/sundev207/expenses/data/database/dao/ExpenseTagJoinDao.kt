package com.sundev207.expenses.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomWarnings
import com.sundev207.expenses.data.ExpenseTagJoin
import com.sundev207.expenses.data.Tag

@Dao
interface ExpenseTagJoinDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("""
        SELECT * FROM tags INNER JOIN expense_tag_joins ON
        tags.id = expense_tag_joins.tag_id WHERE
        expense_tag_joins.expense_id = :expenseId
        """)
    fun getTagsWithExpenseId(expenseId: Long): List<Tag>

    @Insert
    fun insert(join: ExpenseTagJoin)
}