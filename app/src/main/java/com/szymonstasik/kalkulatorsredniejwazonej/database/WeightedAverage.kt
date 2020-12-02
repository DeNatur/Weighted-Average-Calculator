package com.szymonstasik.kalkulatorsredniejwazonej.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "weighted_average_table")
data class WeightedAverage(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "time_added_milli")
    val timeAddedMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "weighted_average_name")
    var name: String = "",

    @ColumnInfo(name = "notes")
    var notes: List<NoteNWeight>,

    @ColumnInfo(name = "tags")
    var tags: List<AverageTag> = ArrayList()
)

@Entity(tableName = "average_tags_table")
data class AverageTag(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "color")
    var color: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = ""
)

data class NoteNWeight(
    var id: UUID = UUID.randomUUID(),
    var note: Int = 1,
    var weight: Int = 0
)
