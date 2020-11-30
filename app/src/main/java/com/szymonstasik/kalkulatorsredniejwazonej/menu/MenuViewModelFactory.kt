package com.szymonstasik.kalkulatorsredniejwazonej.menu

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverageDao

class MenuViewModelFactory () : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
                return MenuViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}
