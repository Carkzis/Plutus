package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.plutus.data.DatabaseCpiInflationRate

@Dao
interface CpiDao {

    @Query("SELECT * FROM DatabaseCpiInflationRate")
    fun getCpiRates(): LiveData<List<DatabaseCpiInflationRate>>

    @Query("SELECT * FROM DatabaseCpiInflationRate")
    fun getCpiRateList(): List<DatabaseCpiInflationRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseCpiInflationRate>)

}