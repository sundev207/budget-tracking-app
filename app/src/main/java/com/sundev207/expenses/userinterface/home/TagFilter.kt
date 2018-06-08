package com.sundev207.expenses.userinterface.home

import android.os.Parcelable
import com.sundev207.expenses.data.Tag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagFilter(val tags: HashSet<Tag>): Parcelable {

    constructor() : this(HashSet())

    fun add(tag: Tag) = tags.add(tag)

    fun remove(tag: Tag) = tags.remove(tag)
}