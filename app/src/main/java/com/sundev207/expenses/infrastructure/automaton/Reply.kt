package com.sundev207.expenses.infrastructure.automaton

import io.reactivex.Observable

data class Reply<State, Input>(
        val input: Input,
        val fromState: State,
        val toState: State,
        val output: Observable<Input>?
)