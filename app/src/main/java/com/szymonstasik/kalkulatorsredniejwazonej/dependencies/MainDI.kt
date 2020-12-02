package com.szymonstasik.kalkulatorsredniejwazonej.dependencies

/**
 * Root DI component with list of multiple dependencies.
 */
val appComponent = listOf(
    viewModelDependency,
    databaseDependency,
    calculatorStateDependency
)