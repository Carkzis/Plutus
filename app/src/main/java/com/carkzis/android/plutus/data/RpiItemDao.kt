package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Dao for performing queries on the Retail Price Index (RPI) items
 * (that is, the indices for different months) in the Room database.
 */
@Dao
interface RpiItemDao {

    @Query("SELECT * FROM DatabaseRpiItem")
    fun getRpiItems(): LiveData<List<DatabaseRpiItem>>

    @Query("SELECT * FROM DatabaseRpiItem")
    fun getRpiItemList(): List<DatabaseRpiItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rpiItems: List<DatabaseRpiItem>)
}