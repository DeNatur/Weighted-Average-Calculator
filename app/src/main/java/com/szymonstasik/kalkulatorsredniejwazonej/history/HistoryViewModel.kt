package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult.TagChooser
import com.szymonstasik.kalkulatorsredniejwazonej.core.BaseViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.database.*
import com.szymonstasik.kalkulatorsredniejwazonej.utils.CalculatorState
import kotlinx.coroutines.*
import org.koin.core.component.inject

class HistoryViewModel(context: Application): BaseViewModel(context) {

    private val calculatorState: CalculatorState by inject()

    private val dataSource: WeightedAverageDatabase by inject();
    private val database: WeightedAverageDao = dataSource.weightedAverageDao;

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
    private val _navigateToCalculator = MutableLiveData<Boolean>()

    val navigateToCalculator: LiveData<Boolean>
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

    private val _listOfWeightedAveragesChooser = MutableLiveData<List<WeightedAverageChooser>>()

    val listOfWeightedAveragesChooser: LiveData<List<WeightedAverageChooser>>
        get(){
            return  _listOfWeightedAveragesChooser
        }

    private val _listOfWeightAverages = MediatorLiveData<List<WeightedAverage>>()

    val listOfWeightedAverage: LiveData<List<WeightedAverage>>
        get() {
            return _listOfWeightAverages
        }

    init {
        _listOfWeightAverages.addSource(database.getAllWeightedAverages(), _listOfWeightAverages::setValue)
        val tmpArray = ArrayList<WeightedAverageChooser>()
        _listOfWeightedAveragesChooser.value = ArrayList()
        uiScope.launch {
            val allWeightedAverages = getAllWeightedAverages()
            for (avg in allWeightedAverages){
                tmpArray.add(WeightedAverageChooser(weightedAverage = avg, chosen = false))
            }
            _listOfWeightedAveragesChooser.value = tmpArray
        }
    }

    fun onFABClick(){
        calculatorState.setNewWeightedAverage()
        _navigateToCalculator.value = true
    }

    fun onEditClick(weightedAverage: WeightedAverage){
        calculatorState.currentWeightedAverage = weightedAverage
        _navigateToCalculator.value = true
    }

    fun changeWeightedAverageToChosen(weightedAverageChooser: WeightedAverageChooser){
        val listOfTags: ArrayList<WeightedAverageChooser> = _listOfWeightedAveragesChooser.value as ArrayList<WeightedAverageChooser>
        val index = listOfTags.indexOf(weightedAverageChooser)
        listOfTags.remove(weightedAverageChooser)
        weightedAverageChooser.chosen = !weightedAverageChooser.chosen
        listOfTags.add(index, weightedAverageChooser)
        _listOfWeightedAveragesChooser.value = listOfTags
    }

    fun onPressDelete(){
        uiScope.launch {
            val avgToDelete = ArrayList<WeightedAverage>()
            val allTagChoosers = _listOfWeightedAveragesChooser.value as ArrayList
            val endTags = ArrayList<WeightedAverageChooser>()
            for(avg in allTagChoosers){
                if(avg.chosen){
                    avgToDelete.add(avg.weightedAverage)
                }else{
                    endTags.add(avg)
                }
            }
            for(avg in avgToDelete){
                delete(avg)
            }
            _listOfWeightedAveragesChooser.value = endTags
        }

    }


    fun doneNavigationToCalculator(){
        _navigateToCalculator.value = false
    }

    private suspend fun getAllWeightedAverages() : List<WeightedAverage>{
        return withContext(Dispatchers.IO){
            database.getAllAverages()
        }
    }

    private suspend fun delete(weightedAverage: WeightedAverage){
        withContext( Dispatchers.IO){
            database.delete(weightedAverage)
        }
    }
}