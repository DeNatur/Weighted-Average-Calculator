package com.szymonstasik.kalkulatorsredniejwazonej.history

import android.app.Application
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.szymonstasik.kalkulatorsredniejwazonej.R
import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage
import com.szymonstasik.kalkulatorsredniejwazonej.utils.StrokeTextView
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Utils
import java.text.DateFormat

@BindingAdapter("weightedAverageName")
fun TextView.setName(item: WeightedAverage?) {
    item?.let {
        text =
            if (item.name == ""){
                context.getString(R.string.no_name)
            }else{
                item.name
            }
    }
}


@BindingAdapter("weightedAverageDate")
fun TextView.setDate(item: WeightedAverage?) {
    item?.let {
        var txt = resources.getText(R.string.date)
        var date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(item.timeAddedMilli).toString();
        text = "$txt $date"
    }
}

@BindingAdapter("weightedAverage")
fun TextView.setWeightedAverage(item: WeightedAverage?) {
    item?.let {
        val resultNoteList = item.notes
        var allWeights = 0f
        var allValues = 0f
        for (notesNWeight in resultNoteList){
            var weight: Float = notesNWeight.weight + 1f
            allWeights += weight
            var note: Float = Utils.getNoteFromId(notesNWeight.note)
            allValues +=  note * weight
        }
        text = context.getString(R.string.weighted_average_value, allValues/allWeights)
    }
}