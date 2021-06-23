package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CpiItemDao {

    @Query("SELECT * FROM DatabaseCpiItem")
    fun getCpiItems(): LiveData<List<DatabaseCpiItem>>

    @Query("SELECT * FROM DatabaseCpiItem")
    fun getCpiItemList(): List<DatabaseCpiItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rpiItems: List<DatabaseCpiItem>)

}