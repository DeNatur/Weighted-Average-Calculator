package com.szymonstasik.kalkulatorsredniejwazonej.dependencies

import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import com.szymonstasik.kalkulatorsredniejwazonej.utils.CalculatorState
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val calculatorStateDependency = module {
    single { CalculatorState() }
}