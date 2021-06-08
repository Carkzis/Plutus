package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CpiDao {

    @Query("SELECT * FROM DatabaseCpiInflationRate")
    fun getCpiRates(): LiveData<List<DatabaseCpiInflationRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseCpiInflationRate>)

}