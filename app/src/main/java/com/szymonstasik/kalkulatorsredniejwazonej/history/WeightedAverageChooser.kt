package com.szymonstasik.kalkulatorsredniejwazonej.history

import com.szymonstasik.kalkulatorsredniejwazonej.database.WeightedAverage

data class WeightedAverageChooser(
    var weightedAverage: WeightedAverage,
    var chosen: Boolean = false
)