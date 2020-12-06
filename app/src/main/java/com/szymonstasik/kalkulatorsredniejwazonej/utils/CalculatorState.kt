package com.szymonstasik.kalkulatorsredniejwazonej.utils

import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage

class CalculatorState(){
    private lateinit var _currentWeightedAverage: WeightedAverage

    init {
        setNewWeightedAverage()
    }

    fun setNewWeightedAverage(){
        val tmpArray = ArrayList<NoteNWeight>()
        tmpArray.add(NoteNWeight())
        _currentWeightedAverage = WeightedAverage(notes = tmpArray, tags = ArrayList());
    }


    var currentWeightedAverage: WeightedAverage
        get() {
        return _currentWeightedAverage
    }
    set(value) {
        _currentWeightedAverage = value
    }

}