package com.example.android.plutus.data

interface Repository {
    suspend fun refreshInflation()
}