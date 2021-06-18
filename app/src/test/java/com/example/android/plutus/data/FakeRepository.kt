package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.InflationRate
import javax.inject.Inject

class FakeRepository @Inject constructor() : Repository {

    // This is so that the mapping is tested for.
    var cpiDatabaseRates = MutableLiveData<List<DatabaseCpiInflationRate>>().apply {
        value = MutableList(5) {
            DatabaseCpiInflationRate("01/01/1900",
                "5.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
    }

    override fun getRates(inflationType: String): LiveData<List<InflationRate>> {
        // For now, we will just return the cpi rates.
        return Transformations.map(cpiDatabaseRates) {
            it.asDomainModel()
        }
    }

    // We will set this variable here, for when we are testing the repository.
    var cpiInflationRates = getRates("cpi")

    private var returnError = false

    fun setReturnError(isError: Boolean) {
        returnError = isError
    }

    override suspend fun refreshInflation() {
        // Test if there is an error.
        if (returnError) {
            return
        }

        // Return a new list of fake values, one size larger to imitate returning updated data.
        cpiDatabaseRates.value = MutableList(6) {
            DatabaseCpiInflationRate(
                "12/12/2000",
                "1.0",
                "N/A",
                "2000",
                "December",
                "Q4",
                "N/A",
                "N/A",
                "1"
            )
        }

        cpiInflationRates = getRates("cpi")

    }
}