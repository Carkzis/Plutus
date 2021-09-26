package com.carkzis.android.plutus

import com.carkzis.android.plutus.data.DatabaseCpiItem
import com.carkzis.android.plutus.data.DatabaseCpiPct
import com.carkzis.android.plutus.data.DatabaseRpiItem
import com.carkzis.android.plutus.data.DatabaseRpiPct
import com.squareup.moshi.JsonClass

/**
 * Data object for data received from the network.
 */
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

/**
 * This is the initial container for the received data, and allows us to go one level deep
 * into the "months" key, as we want monthly data (as opposed to, for example, yearly).
 */
@JsonClass(generateAdapter = true)
data class NetworkInflationItemContainer(val months: List<NetworkInflationItem>)

/**
 * Extension function to map a list of CPI 12-month percentages from the network call into
 * the Room database entities, to be stored locally.
 */
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

/**
 * Extension function to map a list of CPI items from the network call into
 * the Room database entities, to be stored locally.
 */
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

/**
 * Extension function to map a list of RPI 12-month percentages from the network call into
 * the Room database entities, to be stored locally.
 */
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

/**
 * Extension function to map a list of RPI items from the network call into
 * the Room database entities, to be stored locally.
 */
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