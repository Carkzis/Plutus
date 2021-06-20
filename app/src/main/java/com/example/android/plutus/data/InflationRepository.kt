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
    override suspend fun refreshCpiInflation() {
        withContext(Dispatchers.IO) {
            val cpiInflationList = InflationRateApi.retrofitService.getCpiInformation()
            database.cpiDao().insertAll(cpiInflationList.asCpiDatabaseModel())
        }
    }

    override suspend fun refreshRpiInflation() {
        withContext(Dispatchers.IO) {
            val rpiInflationList = InflationRateApi.retrofitService.getRpiInformation()
            database.rpiDao().insertAll(rpiInflationList.asRpiDatabaseModel())
        }
    }

    override fun getCpiRates(inflationType: String): LiveData<List<CpiInflationRate>> {
        return Transformations.map(database.cpiDao().getCpiRates()) {
            it.asCpiDomainModel()
        }
    }

    override fun getRpiRates(): LiveData<List<RpiInflationRate>> {
        return Transformations.map(database.rpiDao().getRpiRates()) {
            it.asRpiDomainModel()
        }
    }

}