package com.szymonstasik.kalkulatorsredniejwazonej.menu

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.core.BaseViewModel
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDao
import kotlinx.coroutines.*

class MenuViewModel(context: Application) : BaseViewModel(context) {


    /**
     * Variable that tells the Fragment to navigate to a specific [CalculatorFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() {
            return _navigateToHome
        }

    /**
     * Call this immediately after navigating to [HistoryFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneHomeNavigating() {
        _navigateToHome.value = false
    }

    fun onStartClick(){
        _navigateToHome.value = true
    }
}