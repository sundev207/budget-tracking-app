package com.sundev207.expenses.automaton

import io.reactivex.Observable

class ApplicationMapper: Mapper<ApplicationState, Any> {

    override fun map(state: ApplicationState, input: Any): Pair<ApplicationState, Observable<Any>> {
        TODO("not implemented")
    }
}