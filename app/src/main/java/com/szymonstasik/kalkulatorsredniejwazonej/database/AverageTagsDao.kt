package com.szymonstasik.kalkulatorsredniejwazonej.database

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface AverageTagsDao {

    /**
     * Insert new value to a table
     *
     * @param averageTag new value to write
     */
    @Insert
    fun insert(averageTag: AverageTag): Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param averageTag new value to write
     */
    @Update
    fun update(averageTag: AverageTag)

    /**
     * Deletes selected value from database
     *
     * @param averageTag value to delete
     */
    @Delete
    fun delete(averageTag: AverageTag)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key timeAddedMilli to match
     */
    @Query("SELECT * from average_tags_table WHERE id = :key")
    fun get(key: Long): AverageTag?

//    @Query("SELECT MAX(time_added_milli) from weighted_average_table")
//    fun getLatest(): WeightedAverage?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM average_tags_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM average_tags_table ORDER BY id DESC")
    fun getAllTags(): LiveData<List<AverageTag>>

    /**
     * Selects and returns the weighted average with given id.
     */
    @Query("SELECT * from average_tags_table WHERE id = :key")
    fun getTagWithId(key: Long): LiveData<List<AverageTag>>
}