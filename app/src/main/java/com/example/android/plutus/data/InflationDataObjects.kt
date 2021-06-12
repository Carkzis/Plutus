package com.example.android.plutus

import android.net.Network
import com.example.android.plutus.data.DatabaseCpiInflationRate
import com.squareup.moshi.JsonClass
import timber.log.Timber

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
data class NetworkInflationRateContainer(val months: List<NetworkInflationRate>)

fun NetworkInflationRateContainer.asDomainModel(): List<InflationRate> {

    return months.map {
        InflationRate(
            date = it.date,
            value = it.value + "%",
            label = it.label,
            year = it.year,
            month = it.month,
            quarter = it.quarter,
            sourceDataset = it.sourceDataset,
            updateDate = it.updateDate
        )
    }
}

fun NetworkInflationRateContainer.asDatabaseModel(): List<DatabaseCpiInflationRate> {

    return months.map {
        DatabaseCpiInflationRate(
            date = it.date,
            value = it.value,
            label = it.label,
            year = it.year,
            month = it.month,
            quarter = it.quarter,
            sourceDataset = it.sourceDataset,
            updateDate = it.updateDate,
            pk = it.year + it.month
        )
    }
}