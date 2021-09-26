package com.carkzis.android.plutus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carkzis.android.plutus.CpiItem
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.RpiItem
import com.carkzis.android.plutus.RpiPercentage

/**
 * Table to hold Consumer Price Index (CPI) 12-month percentages.
 */
@Entity
data class DatabaseCpiPct(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String,
    @PrimaryKey
    val pk: String)

/**
 * Table to hold Consumer Price Index (CPI) items.
 */
@Entity
data class DatabaseCpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String,
    @PrimaryKey
    val pk: String)

/**
 * Table to hold Retail Price Index (RPI) 12-month percentages.
 */
@Entity
data class DatabaseRpiPct(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String,
    @PrimaryKey
    val pk: String)

/**
 * Table to hold Retail Price Index (RPI) items.
 */
@Entity
data class DatabaseRpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String,
    @PrimaryKey
    val pk: String)

/**
 * Extension function to map a list of CPI 12-month percentages from the Room database into
 * the domain model presented to the UI.
 */
fun List<DatabaseCpiPct>.asCpiPctDomainModel(): List<CpiPercentage> {
    return map {
        CpiPercentage(
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

/**
 * Extension function to map a list of CPI items from the Room database into
 * the domain model presented to the UI.
 */
fun List<DatabaseCpiItem>.asCpiItemDomainModel(): List<CpiItem> {
    return map {
        CpiItem(
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

/**
 * Extension function to map a list of RPI 12-month percentages from the Room database into
 * the domain model presented to the UI.
 */
fun List<DatabaseRpiPct>.asRpiPctDomainModel(): List<RpiPercentage> {
    return map {
        RpiPercentage(
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

/**
 * Extension function to map a list of RPI items from the Room database into
 * the domain model presented to the UI.
 */
fun List<DatabaseRpiItem>.asRpiItemDomainModel(): List<RpiItem> {
    return map {
        RpiItem(
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