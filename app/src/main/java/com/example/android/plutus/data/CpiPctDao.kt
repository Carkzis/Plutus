package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CpiPctDao {

    @Query("SELECT * FROM DatabaseCpiPct")
    fun getCpiRates(): LiveData<List<DatabaseCpiPct>>

    @Query("SELECT * FROM DatabaseCpiPct")
    fun getCpiRateList(): List<DatabaseCpiPct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseCpiPct>)

}