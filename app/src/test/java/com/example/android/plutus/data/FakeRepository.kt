package com.example.android.plutus.data

import com.example.android.plutus.InflationRate
import com.example.android.plutus.NetworkInflationRate
import org.junit.Assert.*
import javax.inject.Inject

class FakeRepository @Inject constructor() : Repository {

    // This is so that the mapping is tested for.
    private var cpiNetworkRates: List<NetworkInflationRate> = mutableListOf()

    var cpiInflationRates: List<InflationRate> = mutableListOf()

    private var returnError = true

    fun setReturnError(isError: Boolean) {
        returnError = isError
    }

    override suspend fun refreshInflation() {
        // TODO: Essentially, this will just return a fake list of InflationRates increased by one
        // TODO: Will map it to from the cpiNetworkRates, like the usual repository.
    }
}