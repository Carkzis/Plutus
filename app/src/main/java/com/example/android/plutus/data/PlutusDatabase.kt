package com.example.android.plutus.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseCpiPct::class, DatabaseRpiPct::class, DatabaseRpiItem::class,
                     DatabaseCpiItem::class],
    version = 6, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiPctDao
    abstract fun rpiDao(): RpiPctDao
    abstract fun cpiItemDao(): CpiItemDao
    abstract fun rpiItemDao(): RpiItemDao

}