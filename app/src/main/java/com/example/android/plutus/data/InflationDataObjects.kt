package com.example.android.plutus

import android.net.Network
import com.example.android.plutus.data.DatabaseCpiInflationRate
import com.example.android.plutus.data.DatabaseRpiInflationRate
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

fun NetworkInflationRateContainer.asCpiDatabaseModel(): List<DatabaseCpiInflationRate> {

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

fun NetworkInflationRateContainer.asRpiDatabaseModel(): List<DatabaseRpiInflationRate> {

    return months.map {
        DatabaseRpiInflationRate(
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