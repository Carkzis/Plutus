package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import com.example.android.plutus.InflationRate

interface Repository {

    suspend fun refreshInflation()
    fun getRates(inflationType: String): LiveData<List<InflationRate>>

}