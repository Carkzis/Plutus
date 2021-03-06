package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Dao for performing queries on the Retail Price Index (RPI) 12-month percentage changes
 * in the Room database.
 */
@Dao
interface RpiPctDao {

    @Query("SELECT * FROM DatabaseRpiPct")
    fun getRpiRates(): LiveData<List<DatabaseRpiPct>>

    @Query("SELECT * FROM DatabaseRpiPct")
    fun getRpiRateList(): List<DatabaseRpiPct>

    @Query("SELECT * FROM DatabaseRpiPct WHERE month = 'September'")
    fun getRpiRatesForReval(): LiveData<List<DatabaseRpiPct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseRpiPct>)

}