package com.example.android.plutus.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.plutus.data.DatabaseCpiInflationRate

@Database(entities = [DatabaseCpiInflationRate::class], version = 2, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiDao

}