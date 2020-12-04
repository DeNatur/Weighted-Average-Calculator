package com.szymonstasik.kalkulatorsredniejwazonej.database

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface WeightedAverageDao {


    /**
     * Insert new value to a table
     *
     * @param weightedAverage new value to write
     */
    @Insert
    fun insert(weightedAverage: WeightedAverage): Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param weightedAverage new value to write
     */
    @Update
    fun update(weightedAverage: WeightedAverage)

    /**
     * Deletes selected value from database
     *
     * @param weightedAverage value to delete
     */
    @Delete
    fun delete(weightedAverage: WeightedAverage)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key timeAddedMilli to match
     */
    @Query("SELECT * from weighted_average_table WHERE id = :key")
    fun get(key: Long): WeightedAverage?

//    @Query("SELECT MAX(time_added_milli) from weighted_average_table")
//    fun getLatest(): WeightedAverage?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM weighted_average_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM weighted_average_table ORDER BY id DESC")
    fun getAllWeightedAverages(): LiveData<List<WeightedAverage>>

    /**
     * Selects and returns the weighted average with given id.
     */
    @Query("SELECT * from weighted_average_table WHERE id = :key")
    fun getWeightedAverageWithId(key: Long): LiveData<List<WeightedAverage>>
}