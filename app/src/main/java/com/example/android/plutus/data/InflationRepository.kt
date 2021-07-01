package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationRepository(private val database: PlutusDatabase) : Repository {

    /**
     * This will obtain data from the ONS website and insert it into the database.
     */
    override suspend fun refreshCpiPercentages() {
        withContext(Dispatchers.IO) {
            val cpiInflationList = InflationRateApi.retrofitService.getCpiPctInformation()
            database.cpiDao().insertAll(cpiInflationList.asCpiPctDatabaseModel())
        }
    }

    override suspend fun refreshRpiPercentages() {
        withContext(Dispatchers.IO) {
            val rpiInflationList = InflationRateApi.retrofitService.getRpiPctInformation()
            database.rpiDao().insertAll(rpiInflationList.asRpiPctDatabaseModel())
        }
    }

    override suspend fun refreshCpiItems() {
        withContext(Dispatchers.IO) {
            val cpiItemList = InflationRateApi.retrofitService.getCpiItemInformation()
            database.cpiItemDao().insertAll(cpiItemList.asCpiItemDatabaseModel())
        }
    }

    override suspend fun refreshRpiItems() {
        withContext(Dispatchers.IO) {
            val rpiItemList = InflationRateApi.retrofitService.getRpiItemInformation()
            database.rpiItemDao().insertAll(rpiItemList.asRpiItemDatabaseModel())
        }
    }

    override fun getCpiPercentages(): LiveData<List<CpiPercentage>> {
        return Transformations.map(database.cpiDao().getCpiRates()) {
            it.asCpiPctDomainModel()
        }
    }

    override fun getSeptemberCpi(): LiveData<List<CpiPercentage>> {
        return Transformations.map(database.cpiDao().getCpiRates()) {
            it.asCpiPctDomainModel()
        }
    }

    override fun getRpiPercentages(): LiveData<List<RpiPercentage>> {
        return Transformations.map(database.rpiDao().getRpiRates()) {
            it.asRpiPctDomainModel()
        }
    }

    override fun getSeptemberRpi(): LiveData<List<RpiPercentage>> {
        return Transformations.map(database.rpiDao().getRpiRatesForReval()) {
            it.asRpiPctDomainModel()
        }
    }

    override fun getCpiItems(): LiveData<List<CpiItem>> {
        return Transformations.map(database.cpiItemDao().getCpiItems()) {
            it.asCpiItemDomainModel()
        }
    }

    override fun getRpiItems(): LiveData<List<RpiItem>> {
        return Transformations.map(database.rpiItemDao().getRpiItems()) {
            it.asRpiItemDomainModel()
        }
    }

}