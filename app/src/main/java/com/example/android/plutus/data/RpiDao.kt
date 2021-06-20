package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RpiDao {

    @Query("SELECT * FROM DatabaseRpiPct")
    fun getRpiRates(): LiveData<List<DatabaseRpiPct>>

    @Query("SELECT * FROM DatabaseRpiPct")
    fun getRpiRateList(): List<DatabaseRpiPct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseRpiPct>)

}