package com.carkzis.android.plutus.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database to hold locally the inflation data obtained from the Office for National Statistics.
 * Will be injected using Hilt.
 */
@Database(entities = [DatabaseCpiPct::class, DatabaseRpiPct::class, DatabaseRpiItem::class,
                     DatabaseCpiItem::class],
    version = 6, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiPctDao
    abstract fun rpiDao(): RpiPctDao
    abstract fun cpiItemDao(): CpiItemDao
    abstract fun rpiItemDao(): RpiItemDao

}