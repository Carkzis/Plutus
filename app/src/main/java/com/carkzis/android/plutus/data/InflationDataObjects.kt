package com.carkzis.android.plutus

import com.carkzis.android.plutus.data.DatabaseCpiItem
import com.carkzis.android.plutus.data.DatabaseCpiPct
import com.carkzis.android.plutus.data.DatabaseRpiItem
import com.carkzis.android.plutus.data.DatabaseRpiPct
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkInflationItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

@JsonClass(generateAdapter = true)
data class NetworkInflationItemContainer(val months: List<NetworkInflationItem>)

fun NetworkInflationItemContainer.asCpiPctDatabaseModel(): List<DatabaseCpiPct> {

    return months.map {
        DatabaseCpiPct(
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

fun NetworkInflationItemContainer.asCpiItemDatabaseModel(): List<DatabaseCpiItem> {

    return months.map {
        DatabaseCpiItem(
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

fun NetworkInflationItemContainer.asRpiPctDatabaseModel(): List<DatabaseRpiPct> {

    return months.map {
        DatabaseRpiPct(
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

fun NetworkInflationItemContainer.asRpiItemDatabaseModel(): List<DatabaseRpiItem> {

    return months.map {
        DatabaseRpiItem(
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