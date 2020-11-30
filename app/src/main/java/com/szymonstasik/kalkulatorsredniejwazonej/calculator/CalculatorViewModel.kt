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
import kotlinx.coroutines.*
import org.koin.core.component.inject
import java.util.*
import kotlin.collections.ArrayList

class CalculatorViewModel(context: Application, private val weightedAverageKey: Long = 0L): BaseViewModel(context) {

    private val dataSource: WeightedAverageDatabase by inject()
    private val database: WeightedAverageDao = dataSource.weightedAverageDao
    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    /**
     * Variable that tells the Fragment to navigate to a specific [ResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */

    private val _navigateToResults = MutableLiveData<Long>()

    val navigateToResult: LiveData<Long>
    get() {
        return _navigateToResults
    }

    private val _backPressState = MutableLiveData<Boolean>()

    val backPressState: LiveData<Boolean>
    get(){
        return _backPressState
    }

    private val _weightedAverage = MutableLiveData<WeightedAverage>()

    val weightedAverage: LiveData<WeightedAverage>
        get() {
            return _weightedAverage
        }

    fun onCalculateClick(){
        uiScope.launch {
            _weightedAverage.value?.let { update(it) }
            _navigateToResults.value = weightedAverageKey
        }
    }

    fun doneNavigatingToResult(){
        _navigateToResults.value = null
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
    fun donePopBack(){
        _backPressState.value = false
    }
    fun onBackPressed(){
        _backPressState.value = true
    }

    init {
        _backPressState.value = false
        uiScope.launch {
            _weightedAverage.value = getWeightedAverage(weightedAverageKey)
        }
    }

    private suspend fun getWeightedAverage(key: Long) : WeightedAverage? {
        return withContext(Dispatchers.IO) {
            database.get(key)
        }
    }

    private suspend fun update(weightedAverage: WeightedAverage) {
        withContext(Dispatchers.IO) {
            database.update(weightedAverage)
        }
    }
}