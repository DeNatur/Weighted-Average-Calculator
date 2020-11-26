package com.szymonstasik.kalkulatorsredniejwazonej.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager


object Utils{
    fun getNoteFromId(position: Int) : Float{
        var value = 0f;
        for(i in 0..position){
            value += 0.5f;
        }
        return value
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    fun isWhole(value: Double):Boolean {
        return value - value.toInt() == 0.0
    }
}