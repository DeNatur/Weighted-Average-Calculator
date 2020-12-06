package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Statics
import com.szymonstasik.kalkulatorsredniejwazonej.utils.StrokeTextView
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils

@BindingAdapter("note")
fun TextView.setNote(item: NoteNWeight?) {
    item?.let {
        var value = Utils.getNoteFromId(item.note);
        text = if (Utils.isWhole(value.toDouble()))
            value.toInt().toString()
        else
            "${value.toInt()} +"

    }
}

@BindingAdapter("weight")
fun TextView.setWeight(item: NoteNWeight?) {
    item?.let {
        text = (item.weight + 1).toString()
    }
}

@BindingAdapter("chosenTag")
fun LinearLayout.setChosenTag(item: TagChooser?) {
    item?.let {
        Log.d("chosen", "1234")
        val drawable = background as GradientDrawable
        drawable.setStroke(6, resources.getColor(item.averageTag.color))
        if(item.chosen){
            drawable.setColor(resources.getColor(item.averageTag.color))
        }
    }
}

@BindingAdapter("chosenTag")
fun TextView.setChosenTag(item: TagChooser?) {
    item?.let {
        if(item.chosen)
            setTextColor(resources.getColor(R.color.white))
        else
            setTextColor(resources.getColor(R.color.bg_start))
        text = item.averageTag.name
    }
}