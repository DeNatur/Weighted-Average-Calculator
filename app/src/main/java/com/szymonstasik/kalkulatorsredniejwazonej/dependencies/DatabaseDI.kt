package com.szymonstasik.kalkulatorsredniejwazonej.dependencies

import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseDependency = module {
    single {  WeightedAverageDatabase.getInstance(androidContext())}
}