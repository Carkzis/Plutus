package com.carkzis.android.plutus.data

import androidx.lifecycle.LiveData
import com.carkzis.android.plutus.CpiItem
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.RpiItem
import com.carkzis.android.plutus.RpiPercentage

interface Repository {

    suspend fun refreshCpiPercentages()
    fun getCpiPercentages(): LiveData<List<CpiPercentage>>
    fun getSeptemberCpi(): LiveData<List<CpiPercentage>>

    suspend fun refreshRpiPercentages()
    fun getRpiPercentages(): LiveData<List<RpiPercentage>>
    fun getSeptemberRpi(): LiveData<List<RpiPercentage>>

    suspend fun refreshCpiItems()
    fun getCpiItems(): LiveData<List<CpiItem>>

    suspend fun refreshRpiItems()
    fun getRpiItems(): LiveData<List<RpiItem>>

}