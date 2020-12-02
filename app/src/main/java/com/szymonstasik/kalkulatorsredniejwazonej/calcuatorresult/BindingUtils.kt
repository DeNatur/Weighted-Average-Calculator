package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import android.widget.TextView
import androidx.databinding.BindingAdapter
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