package com.szymonstasik.kalkulatorsredniejwazonej.calculator

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.core.BaseViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDao
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDatabase
import com.szymonstasik.kalkulatorsredniejwazonej.utils.CalculatorState
import kotlinx.coroutines.*
import org.koin.core.component.inject
import java.util.*
import kotlin.collections.ArrayList

class CalculatorViewModel(context: Application): BaseViewModel(context) {

    private val calculatorState: CalculatorState by inject()

    /**
     * Variable that tells the Fragment to navigate to a specific [ResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */

    private val _navigateToResults = MutableLiveData<Boolean>()

    val navigateToResult: LiveData<Boolean>
    get() {
        return _navigateToResults
    }

    private val _backPressState = MutableLiveData<Boolean>()

    val backPressState: LiveData<Boolean>

    get(){
        return _backPressState
    }

    fun donePopBack(){
        _backPressState.value = false
    }

    fun onBackPressed(){
        _backPressState.value = true
    }

    private val _weightedAverage = MutableLiveData<WeightedAverage>()

    val weightedAverage: LiveData<WeightedAverage>
        get() {
            return _weightedAverage
        }

    init {
        _weightedAverage.value = calculatorState.currentWeightedAverage
        _backPressState.value = false
    }

    fun onCalculateClick(){
        val average = _weightedAverage.value;
        if(average != null){
            calculatorState.currentWeightedAverage = average
            _navigateToResults.value = true
        }
    }

    fun doneNavigatingToResult(){
        _navigateToResults.value = false
    }

    fun addNewNote(){
        val note = _weightedAverage.value
        val list = mutableListOf<NoteNWeight>()
        note?.notes?.let { list.addAll(it) }
        list.add(NoteNWeight())
        note?.notes = list
        _weightedAverage.value = note
    }


    fun deleteLastNote(){
        val note = _weightedAverage.value
        val list = mutableListOf<NoteNWeight>()
        note?.notes?.let { list.addAll(it) }
        if(list.size > 0){
            list.removeAt(list.size -1)
            note?.notes = list
            _weightedAverage.value = note
        }
    }

    /**
     *  Method only to use for debugging if values are inserted correctly to livedata
     */
    fun printValues(){
        val note = _weightedAverage.value
        val noteNWeights = note?.notes
        if (noteNWeights != null) {
            for (note in noteNWeights){
                Log.d("Notes ", note.note.toString() + " " + note.weight.toString())
            }
        }
    }

    fun changeValueOfNote(position: Int, value: Int){
        val note = _weightedAverage.value
        val list = mutableListOf<NoteNWeight>()
        note?.notes?.let { list.addAll(it) }
        list[position].note = value
        note?.notes = list
        _weightedAverage.value = note
    }

    fun changeValueOfWeight(position: Int, value: Int){
        val note = _weightedAverage.value
        val list = mutableListOf<NoteNWeight>()
        note?.notes?.let { list.addAll(it) }
        list[position].weight = value
        note?.notes = list
        _weightedAverage.value = note
    }



}