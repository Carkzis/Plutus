package com.example.android.plutus.data

import androidx.lifecycle.LiveData
import com.example.android.plutus.CpiItem
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiItem
import com.example.android.plutus.RpiPercentage

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