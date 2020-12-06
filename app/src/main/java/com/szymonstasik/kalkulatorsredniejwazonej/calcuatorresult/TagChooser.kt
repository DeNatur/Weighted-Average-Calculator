package com.szymonstasik.kalkulatorsredniejwazonej.calcuatorresult

import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag

data class TagChooser(
    var averageTag: AverageTag,
    var chosen: Boolean = false
)
