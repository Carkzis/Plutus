package com.example.android.plutus

import android.net.Network
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InflationNetworkResponse(
    val months: List<NetworkInflationRate>
)

@JsonClass(generateAdapter = true)
data class NetworkInflationRate(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

@JsonClass(generateAdapter = true)
data class NetworkInflationRateContainer(val inflationRates: InflationNetworkResponse)

fun NetworkInflationRateContainer.asDomainModel(): List<InflationRate> {
    return inflationRates.months.map {
        InflationRate(
            date = it.date,
            value = it.value,
            label = it.label,
            year = it.year,
            month = it.month,
            quarter = it.quarter,
            sourceDataset = it.sourceDataset,
            updateDate = it.updateDate
        )
    }
}