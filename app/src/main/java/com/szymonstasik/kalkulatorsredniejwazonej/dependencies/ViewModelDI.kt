package com.szymonstasik.kalkulatorsredniejwazonej.dependencies

import com.szymonstasik.kalkulatorsredniejwazonej.calculator.CalculatorViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.menu.MenuViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelDependency = module {

    viewModel { MenuViewModel(androidApplication()) }
    viewModel { CalculatorViewModel(androidApplication()) }
}