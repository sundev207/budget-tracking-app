package com.sundev207.expenses.automaton

import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.data.preference.PreferenceDataSource
import com.sundev207.expenses.infrastructure.automaton.Automaton

class ApplicationAutomaton(
        databaseDataSource: DatabaseDataSource,
        preferenceDataSource: PreferenceDataSource
) : Automaton<ApplicationState, ApplicationInput>(ApplicationState.INITIAL,
        ApplicationMapper(databaseDataSource,
                preferenceDataSource))