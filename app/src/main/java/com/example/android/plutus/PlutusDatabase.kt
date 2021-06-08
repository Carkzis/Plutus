package com.example.android.plutus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseCpiInflationRate::class], version = 1, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiDao

}

private lateinit var INSTANCE: PlutusDatabase

fun getDatabase(context: Context): PlutusDatabase {
    synchronized(PlutusDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, PlutusDatabase::class.java,
            "cpiRates").build()
        }
    }
    return INSTANCE
}