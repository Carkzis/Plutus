package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.carkzis.android.plutus.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class which is used to abstract access between the data sources
 * (the remote Office for National Statistics API, and the local Room Data), and the UI.
 */
class InflationRepository(private val database: PlutusDatabase) : Repository {

    /**
     * This will obtain CPI 12-month percentage data from the ONS website and
     * insert it into the database.
     */
    override suspend fun refreshCpiPercentages() {
        withContext(Dispatchers.IO) {
            val cpiInflationList = InflationRateApi.retrofitService.getCpiPctInformation()
            database.cpiDao().insertAll(cpiInflationList.asCpiPctDatabaseModel())
        }
    }

    /**
     * This will obtain RPI 12-month percentage data from the ONS website and
     * insert it into the database.
     */
    override suspend fun refreshRpiPercentages() {
        withContext(Dispatchers.IO) {
            val rpiInflationList = InflationRateApi.retrofitService.getRpiPctInformation()
            database.rpiDao().insertAll(rpiInflationList.asRpiPctDatabaseModel())
        }
    }

    /**
     * This will obtain CPI items (not percentages, but the indices per month)
     * from the ONS website and insert it into the database.
     */
    override suspend fun refreshCpiItems() {
        withContext(Dispatchers.IO) {
            val cpiItemList = InflationRateApi.retrofitService.getCpiItemInformation()
            database.cpiItemDao().insertAll(cpiItemList.asCpiItemDatabaseModel())
        }
    }

    /**
     * This will obtain RPI items (not percentages, but the indices per month)
     * from the ONS website and insert it into the database.
     */
    override suspend fun refreshRpiItems() {
        withContext(Dispatchers.IO) {
            val rpiItemList = InflationRateApi.retrofitService.getRpiItemInformation()
            database.rpiItemDao().insertAll(rpiItemList.asRpiItemDatabaseModel())
        }
    }

    /**
     * This retrieves the CPI 12-month percentages from the Room database as LiveData.
     * It comes in a reversed order from that retrieved from the ONS website, that that
     * it is latest entry first when displayed to the UI.
     */
    override fun getCpiPercentages(): LiveData<List<CpiPercentage>> {
        return Transformations.map(database.cpiDao().getCpiRates()) {
            it.reversed().asCpiPctDomainModel()
        }
    }

    /**
     * This retrieves the RPI 12-month percentages from the Room database as LiveData.
     * It comes in a reversed order from that retrieved from the ONS website, that that
     * it is latest entry first when displayed to the UI.
     */
    override fun getRpiPercentages(): LiveData<List<RpiPercentage>> {
        return Transformations.map(database.rpiDao().getRpiRates()) {
            it.reversed().asRpiPctDomainModel()
        }
    }

    /**
     * This retrieves the CPI indices from the Room database as LiveData.
     * It comes in a reversed order from that retrieved from the ONS website, that that
     * it is latest entry first when displayed to the UI.
     */
    override fun getCpiItems(): LiveData<List<CpiItem>> {
        return Transformations.map(database.cpiItemDao().getCpiItems()) {
            it.reversed().asCpiItemDomainModel()
        }
    }

    /**
     * This retrieves the RPI indices from the Room database as LiveData.
     * It comes in a reversed order from that retrieved from the ONS website, that that
     * it is latest entry first when displayed to the UI.
     */
    override fun getRpiItems(): LiveData<List<RpiItem>> {
        return Transformations.map(database.rpiItemDao().getRpiItems()) {
            it.reversed().asRpiItemDomainModel()
        }
    }

    /**
     * Retrieves the CPI 12-month percentages as LiveData, but just for the months of
     * September each year.  Used to calculate CPI revaluation.
     */
    override fun getSeptemberCpi(): LiveData<List<CpiPercentage>> {
        return Transformations.map(database.cpiDao().getCpiRatesForReval()) {
            it.asCpiPctDomainModel()
        }
    }

    /**
     * Retrieves the RPI 12-month percentages as LiveData, but just for the months of
     * September each year. Used to calculate RPI revaluation.
     */
    override fun getSeptemberRpi(): LiveData<List<RpiPercentage>> {
        return Transformations.map(database.rpiDao().getRpiRatesForReval()) {
            it.asRpiPctDomainModel()
        }
    }

}