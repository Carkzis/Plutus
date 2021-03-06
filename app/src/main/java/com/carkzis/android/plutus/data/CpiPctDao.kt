package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Dao for performing queries on the Consumer Price Index (CPI) 12-month percentage changes
 * in the Room database.
 */
@Dao
interface CpiPctDao {

    @Query("SELECT * FROM DatabaseCpiPct")
    fun getCpiRates(): LiveData<List<DatabaseCpiPct>>

    @Query("SELECT * FROM DatabaseCpiPct")
    fun getCpiRateList(): List<DatabaseCpiPct>

    @Query("SELECT * FROM DatabaseCpiPct WHERE month = 'September'")
    fun getCpiRatesForReval(): LiveData<List<DatabaseCpiPct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseCpiPct>)

}