package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.plutus.CpiItem
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiItem
import com.example.android.plutus.RpiPercentage
import javax.inject.Inject

class FakeRepository @Inject constructor() : Repository {

    // This is so that the mapping is tested for.
    // Additional note: for our purposes, all items will have a month of September.
    var cpiDatabaseRates = MutableLiveData<List<DatabaseCpiPct>>().apply {
        value = MutableList(5) {
            DatabaseCpiPct(
                "01/01/1900",
                "5.0",
                "N/A",
                "2020",
                "September",
                "Q1",
                "N/A",
                "N/A",
                "N/A"
            )
        }
    }

    var cpiDatabaseItems = MutableLiveData<List<DatabaseCpiItem>>().apply {
        value = MutableList(10) {
            DatabaseCpiItem(
                "01/01/1900",
                "5.0",
                "N/A",
                "2020",
                "September",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
    }

    var rpiDatabaseRates = MutableLiveData<List<DatabaseRpiPct>>().apply {
        value = MutableList(15) {
            DatabaseRpiPct(
                "01/01/1900",
                "5.0",
                "N/A",
                "2020",
                "September",
                "Q1",
                "N/A",
                "N/A",
                "N/A"
            )
        }
    }

    var rpiDatabaseItems = MutableLiveData<List<DatabaseRpiItem>>().apply {
        value = MutableList(30) {
            DatabaseRpiItem(
                "01/01/1900",
                "5.0",
                "N/A",
                "2020",
                "September",
                "Q1",
                "N/A",
                "N/A",
                "N/A"
            )
        }
    }

    override fun getCpiPercentages(): LiveData<List<CpiPercentage>> {
        // For now, we will just return the cpi rates.
        return Transformations.map(cpiDatabaseRates) {
            it.asCpiPctDomainModel()
        }
    }

    override fun getSeptemberCpi(): LiveData<List<CpiPercentage>> {
        return Transformations.map(cpiDatabaseRates) {
            it.asCpiPctDomainModel()
        }
    }

    override fun getCpiItems(): LiveData<List<CpiItem>> {
        return Transformations.map(cpiDatabaseItems) {
            it.asCpiItemDomainModel()
        }
    }

    override fun getRpiPercentages(): LiveData<List<RpiPercentage>> {
        return Transformations.map(rpiDatabaseRates) {
            it.asRpiPctDomainModel()
        }
    }

    override fun getSeptemberRpi(): LiveData<List<RpiPercentage>> {
        return Transformations.map(rpiDatabaseRates) {
            it.asRpiPctDomainModel()
        }
    }

    override fun getRpiItems(): LiveData<List<RpiItem>> {
        return Transformations.map(rpiDatabaseItems) {
            it.asRpiItemDomainModel()
        }
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
        rpiDatabaseRates = MutableLiveData<List<DatabaseRpiPct>>().apply {
            value = mutableListOf()
        }
        cpiDatabaseItems = MutableLiveData<List<DatabaseCpiItem>>().apply {
            value = mutableListOf()
        }
        rpiDatabaseItems = MutableLiveData<List<DatabaseRpiItem>>().apply {
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
                    "N/A"
                )
            }
        } else if (returnNull) {
            throw Exception("NPE!!!")
        }
    }

    override suspend fun refreshRpiPercentages() {
        // Test if there is an error.
        if (returnError) {
            return
        } else if (!returnNull) {
            // Return a new list of fake values, one size larger to imitate returning updated data.
            rpiDatabaseRates.value = MutableList(16) {
                DatabaseRpiPct(
                    "12/12/2000",
                    "1.0",
                    "N/A",
                    "2000",
                    "December",
                    "Q4",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            }
        } else if (returnNull) {
            throw Exception("NPE!!!")
        }
    }

    override suspend fun refreshCpiItems() {
        // Test if there is an error.
        if (returnError) {
            return
        } else if (!returnNull) {
            // Return a new list of fake values, one size larger to imitate returning updated data.
            cpiDatabaseItems.value = MutableList(11) {
                DatabaseCpiItem(
                    "12/12/2000",
                    "1.0",
                    "N/A",
                    "2000",
                    "December",
                    "Q4",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            }
        } else if (returnNull) {
            throw Exception("NPE!!!")
        }
    }


    override suspend fun refreshRpiItems() {
        // Test if there is an error.
        if (returnError) {
            return
        } else if (!returnNull) {
            // Return a new list of fake values, one size larger to imitate returning updated data.
            rpiDatabaseItems.value = MutableList(31) {
                DatabaseRpiItem(
                    "12/12/2000",
                    "1.0",
                    "N/A",
                    "2000",
                    "December",
                    "Q4",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            }
        } else if (returnNull) {
            throw Exception("NPE!!!")
        }
    }

}