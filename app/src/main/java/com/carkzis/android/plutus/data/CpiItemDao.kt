package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Dao for performing queries on the Consumer Price Index (CPI) items
 * (that is, the indices for different months) in the Room database.
 */
@Dao
interface CpiItemDao {

    @Query("SELECT * FROM DatabaseCpiItem")
    fun getCpiItems(): LiveData<List<DatabaseCpiItem>>

    @Query("SELECT * FROM DatabaseCpiItem")
    fun getCpiItemList(): List<DatabaseCpiItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rpiItems: List<DatabaseCpiItem>)

}