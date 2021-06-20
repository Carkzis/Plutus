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
            database.cpiDao().insertAll(cpiInflationList.asCpiDatabaseModel())
        }
    }

    override suspend fun refreshRpiPercentages() {
        withContext(Dispatchers.IO) {
            val rpiInflationList = InflationRateApi.retrofitService.getRpiPctInformation()
            database.rpiDao().insertAll(rpiInflationList.asRpiDatabaseModel())
        }
    }

    override fun getCpiPercentages(): LiveData<List<CpiPercentage>> {
        return Transformations.map(database.cpiDao().getCpiRates()) {
            it.asCpiDomainModel()
        }
    }

    override fun getRpiPercentages(): LiveData<List<RpiPercentage>> {
        return Transformations.map(database.rpiDao().getRpiRates()) {
            it.asRpiDomainModel()
        }
    }

}