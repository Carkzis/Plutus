package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RpiItemDao {

    @Query("SELECT * FROM DatabaseRpiItem")
    fun getRpiItems(): LiveData<List<DatabaseRpiItem>>

    @Query("SELECT * FROM DatabaseRpiItem")
    fun getRpiItemList(): List<DatabaseRpiItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rpiItems: List<DatabaseRpiItem>)
}