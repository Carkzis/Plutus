package com.example.android.plutus

import android.content.Context
import androidx.room.Room
import com.example.android.plutus.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideCpiDao(database: PlutusDatabase): CpiPctDao {
        return database.cpiDao()
    }

    @Provides
    fun provideRpiDao(database: PlutusDatabase): RpiDao {
        return database.rpiDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PlutusDatabase {
        return Room.databaseBuilder(
            context,
            PlutusDatabase::class.java,
            "inflation"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideRepository(database: PlutusDatabase): Repository {
        return InflationRepository(database)
    }
}