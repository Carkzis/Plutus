package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RpiDao {

    @Query("SELECT * FROM DatabaseRpiInflationRate")
    fun getRpiRates(): LiveData<List<DatabaseRpiInflationRate>>

    @Query("SELECT * FROM DatabaseRpiInflationRate")
    fun getRpiRateList(): List<DatabaseRpiInflationRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cpiRates: List<DatabaseRpiInflationRate>)

}