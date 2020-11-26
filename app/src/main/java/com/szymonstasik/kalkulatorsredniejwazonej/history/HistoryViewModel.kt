package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDao
import kotlinx.coroutines.*

class HistoryViewModel(val database: WeightedAverageDao): ViewModel() {

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
     * Variable that tells the Fragment to navigate to a specific [CalculatorFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToCalculator = MutableLiveData<Long>()

    val navigateToCalculator: LiveData<Long>
        get() {
            return _navigateToCalculator
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

    private val _listOfWeightAverages = MediatorLiveData<List<WeightedAverage>>()

    val listOfWeightedAverage: LiveData<List<WeightedAverage>>
        get() {
            return _listOfWeightAverages
        }

    init {
        _listOfWeightAverages.addSource(database.getAllWeightedAverages(), _listOfWeightAverages::setValue)
    }

    fun onEditClick(weightedAverageKey: Long){
        _navigateToCalculator.value = weightedAverageKey
    }

    fun onDeleteClick(weightedAverage: WeightedAverage){
        uiScope.launch {
            delete(weightedAverage)
        }
    }

    fun doneNavigationToCalculator(){
        _navigateToCalculator.value = null
    }

    fun onCalculatorClick(){
        uiScope.launch {
            var tmpArray = ArrayList<NoteNWeight>()
            tmpArray.add(NoteNWeight())
            val newWeightedAverage = WeightedAverage(
                notes = tmpArray
            )
            _navigateToCalculator.value =  insert(newWeightedAverage)
        }
    }

    private suspend fun insert(weightedAverage: WeightedAverage): Long {
        return withContext(Dispatchers.IO) {
            database.insert(weightedAverage)
        }
    }

    private suspend fun delete(weightedAverage: WeightedAverage){
        withContext( Dispatchers.IO){
            database.delete(weightedAverage)
        }
    }
}