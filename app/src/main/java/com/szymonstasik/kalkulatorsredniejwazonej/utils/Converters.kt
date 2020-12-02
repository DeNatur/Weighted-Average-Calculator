package com.szymonstasik.kalkulatorsredniejwazonej.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.szymonstasik.kalkulatorsredniejwazonej.database.AverageTag
import com.szymonstasik.kalkulatorsredniejwazonej.database.NoteNWeight

class Converters {

    @TypeConverter
    fun listToJson(value: List<NoteNWeight>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<NoteNWeight>? {
        val objects = Gson().fromJson(value, Array<NoteNWeight>::class.java) as Array<NoteNWeight>
        val list = objects.toList()
        return list
    }

    @TypeConverter
    fun fromString(value: List<AverageTag>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromArrayList(value: String): List<AverageTag>? {
        val objects = Gson().fromJson(value, Array<AverageTag>::class.java) as Array<AverageTag>
        val list = objects.toList()
        return list
    }
}