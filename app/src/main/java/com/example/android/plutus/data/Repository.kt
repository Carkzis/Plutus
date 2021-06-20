package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import com.example.android.plutus.CpiInflationRate
import com.example.android.plutus.RpiInflationRate

interface Repository {

    suspend fun refreshCpiInflation()
    fun getCpiRates(inflationType: String): LiveData<List<CpiInflationRate>>

    suspend fun refreshRpiInflation()
    fun getRpiRates(): LiveData<List<RpiInflationRate>>

}