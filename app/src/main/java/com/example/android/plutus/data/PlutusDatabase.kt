package com.example.android.plutus.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.plutus.data.DatabaseCpiInflationRate

@Database(entities = [DatabaseCpiInflationRate::class, DatabaseRpiInflationRate::class],
    version = 3, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiDao
    abstract fun rpiDao(): RpiDao

}