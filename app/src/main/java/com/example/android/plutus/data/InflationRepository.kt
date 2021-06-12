package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.InflationRate
import com.example.android.plutus.InflationRateApi
import com.example.android.plutus.asDatabaseModel
import com.example.android.plutus.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationRepository(private val database: PlutusDatabase) : Repository {

    /**
     * This will obtain data from the ONS website and insert it into the database.
     */
    override suspend fun refreshInflation() {
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