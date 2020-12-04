package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.szymonstasik.kalkulatorsredniejwazonej.core.BaseViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.database.*
import com.szymonstasik.kalkulatorsredniejwazonej.utils.CalculatorState
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils
import kotlinx.coroutines.*
import org.koin.core.component.inject

data class ChosenCircle(
    var circleImageView: ImageView,
    var colorId: Int
)

data class TagChooser(
    var averageTag: AverageTag,
    var chosen: Boolean = false
)

class ResultViewModel(context: Application): BaseViewModel(context) {

    private val calculatorState: CalculatorState by inject()

    private val dataSource: WeightedAverageDatabase by inject()
    private val weightedAverageDatabase: WeightedAverageDao = dataSource.weightedAverageDao
    private val averageTagsDatabase: AverageTagsDao = dataSource.averageTagsDao


    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
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



    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _chosenCircle = MutableLiveData<ChosenCircle>()

    val chosenCircle: LiveData<ChosenCircle>
        get() {
        return _chosenCircle
    }



    fun setChosenCircle(id: ImageView, colorId: Int){
        val circle = ChosenCircle(circleImageView = id, colorId = colorId)
        _chosenCircle.value = circle
    }

    private val _allAverageTags = MutableLiveData<MutableList<TagChooser>>()

    val allAverageTags: LiveData<MutableList<TagChooser>>
        get() {
            return _allAverageTags
        }

    private val _weightedAverage = MutableLiveData<WeightedAverage>()

    val weightedAverage: LiveData<WeightedAverage>
        get() {
            return _weightedAverage
        }

    private val _result = MutableLiveData<Float>()

    private val _navigateToCalculator = MutableLiveData<Long>()

    val navigateToCalculator: LiveData<Long>
        get() {
            return  _navigateToCalculator
        }

    private val _navigateToHistory = MutableLiveData<Boolean>()

    val navigateToHistory: LiveData<Boolean>
        get() {
            return _navigateToHistory
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

    fun onCalculateAgainCLick(){
        uiScope.launch {
            var tmpArray = ArrayList<NoteNWeight>()
            tmpArray.add(NoteNWeight())
            val newWeightedAverage = WeightedAverage(
                notes = tmpArray

            )
            _navigateToCalculator.value =  insert(newWeightedAverage)
        }
    }

    fun onNameNoteNWeightsClick(name: String){
        uiScope.launch {
            _weightedAverage.value?.name = name
            _weightedAverage.value?.let { update(it) }
            _navigateToHistory.value = true
        }
    }

    fun onDoneNavigatingToHistory(){
        _navigateToHistory.value = false
    }

    val result: LiveData<Float>
        get() {
            return _result
        }

    init {
        _weightedAverage.value = calculatorState.currentWeightedAverage
        uiScope.launch {
            val tags = getAllAverageTags()
            val tagsChooser = ArrayList<TagChooser>()
            for (tag in tags){
                if(calculatorState.currentWeightedAverage.tags.contains(tag))
                    tagsChooser.add(TagChooser(tag, true))
                else
                    tagsChooser.add(TagChooser(tag, false))
            }
            _allAverageTags.value = tagsChooser
            instantinateResult()
        }
    }


    private fun instantinateResult() {
        val resultNoteList = _weightedAverage.value?.notes
        var allWeights = 0f;
        var allValues = 0f;
        if (resultNoteList != null) {
            for (notesNWeight in resultNoteList){
                var weight: Float = notesNWeight.weight + 1f
                allWeights += weight
                var note: Float = Utils.getNoteFromId(notesNWeight.note)
                allValues +=  note * weight
            }
            _result.value = allValues / allWeights
        }
    }

    fun addTag(averageTag: AverageTag){
        uiScope.launch {
            insertAverageTag(averageTag);
            allAverageTags.value
        }
    }

    private suspend fun getAllAverageTags(): List<AverageTag> {
        return  withContext(Dispatchers.IO){
            averageTagsDatabase.getAllTags()
        }
    }

    private suspend fun getWeightedAverage(key: Long) : WeightedAverage? {
        return withContext(Dispatchers.IO) {
            weightedAverageDatabase.get(key)
        }
    }

    private suspend fun update(weightedAverage: WeightedAverage) {
        withContext(Dispatchers.IO) {
            weightedAverageDatabase.update(weightedAverage)
        }
    }

    private suspend fun insert(weightedAverage: WeightedAverage): Long {
        return withContext(Dispatchers.IO) {
            weightedAverageDatabase.insert(weightedAverage)
        }
    }

    private suspend fun insertAverageTag(averageTag: AverageTag): Long {
        return withContext(Dispatchers.IO) {
            averageTagsDatabase.insert(averageTag)
        }
    }
}