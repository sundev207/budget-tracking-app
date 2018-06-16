package com.sundev207.expenses.automaton

class ApplicationAutomaton
    : Automaton<ApplicationState, Any>(ApplicationState(), ApplicationMapper())