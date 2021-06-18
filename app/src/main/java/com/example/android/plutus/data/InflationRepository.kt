package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.InflationRate
import com.example.android.plutus.InflationRateApi
import com.example.android.plutus.asDatabaseModel
import com.example.android.plutus.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

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

    override fun getRates(inflationType: String): LiveData<List<InflationRate>> {
        // Different queries are run depending on which information we require.
        val inflationQuery = when(inflationType) {
            "cpi" -> database.cpiDao().getCpiRates()
            else -> throw Exception("Inflation type not recognised!")
        }
        return Transformations.map(inflationQuery) {
            it.asDomainModel()
        }
    }

}