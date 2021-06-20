package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage
import javax.inject.Inject

class FakeRepository @Inject constructor() : Repository {

    // This is so that the mapping is tested for.
    var cpiDatabaseRates = MutableLiveData<List<DatabaseCpiPct>>().apply {
        value = MutableList(5) {
            DatabaseCpiPct(
                "01/01/1900",
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

    override fun getCpiPercentages(): LiveData<List<CpiPercentage>> {
        // For now, we will just return the cpi rates.
        return Transformations.map(cpiDatabaseRates) {
            it.asCpiDomainModel()
        }
    }

    override suspend fun refreshRpiPercentages() {
        TODO("Not yet implemented")
    }

    override fun getRpiPercentages(): LiveData<List<RpiPercentage>> {
        TODO("Not yet implemented")
    }

    // These appear very similar, but return error is just for the repository tests and it
    // less catastrophic, and the return null is for the ViewModel tests as it involves a
    // try catch statement
    private var returnError = false
    private var returnNull = false

    fun setReturnError(isError: Boolean) {
        returnError = isError
    }

    fun setNull(isNull: Boolean) {
        returnNull = isNull
    }

    fun setToEmpty() {
        cpiDatabaseRates = MutableLiveData<List<DatabaseCpiPct>>().apply {
            value = mutableListOf()
        }
    }

    override suspend fun refreshCpiPercentages() {
        // Test if there is an error.
        if (returnError) {
            return
        } else if (!returnNull) {
            // Return a new list of fake values, one size larger to imitate returning updated data.
            cpiDatabaseRates.value = MutableList(6) {
                DatabaseCpiPct(
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
        } else if (returnNull) {
            throw Exception("NPE!!!")
        }
    }
}