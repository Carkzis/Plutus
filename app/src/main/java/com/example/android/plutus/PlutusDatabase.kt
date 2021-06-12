package com.example.android.plutus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseCpiInflationRate::class], version = 2, exportSchema = false)
abstract class PlutusDatabase : RoomDatabase() {

    abstract fun cpiDao(): CpiDao

}