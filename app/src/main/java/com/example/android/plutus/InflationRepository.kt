package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationRepository(private val database: PlutusDatabase) {

    /**
     * This will obtain data from the ONS website and insert it into the database.
     */
    suspend fun refreshInflation() {
        withContext(Dispatchers.IO) {
            val inflationList = InflationRateApi.retrofitService.getCpiInformation()
            database.cpiDao().insertAll(inflationList.asDatabaseModel())
        }
    }

    val cpiInflationRates: LiveData<List<InflationRate>> = Transformations.map(database
        .cpiDao().getCpiRates()) {
        it.asDomainModel()
    }

}