package com.example.android.plutus.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseCpiPct::class, DatabaseRpiPct::class, DatabaseRpiItem::class],
    version = 5, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiPctDao
    abstract fun rpiDao(): RpiPctDao
    abstract fun rpiItemDao(): RpiItemDao

}