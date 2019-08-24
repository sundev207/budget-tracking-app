package com.sundev207.expenses.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sundev207.expenses.data.Tag
import io.reactivex.Observable

@Dao
interface TagDao {

    @Query("SELECT * FROM tags")
    fun observeAll(): Observable<List<Tag>>

    @Query("SELECT * FROM tags")
    fun getAll(): List<Tag>

    @Query("SELECT * FROM tags WHERE name = :name")
    fun getByName(name: String): List<Tag>

    @Insert
    fun insert(tag: Tag): Long

    @Delete
    fun delete(tag: Tag)
}