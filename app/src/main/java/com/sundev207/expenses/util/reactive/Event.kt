package com.sundev207.expenses.util.reactive

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread

class Event {

    private val relay = PublishRelay.create<Any>()

    fun next() = relay.accept(Any())

    @Suppress("HasPlatformType")
    fun toObservable() = (relay as Observable<Any>).observeOn(mainThread())
}