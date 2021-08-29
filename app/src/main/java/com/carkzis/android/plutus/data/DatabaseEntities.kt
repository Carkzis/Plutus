package com.carkzis.android.plutus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carkzis.android.plutus.CpiItem
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.RpiItem
import com.carkzis.android.plutus.RpiPercentage


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