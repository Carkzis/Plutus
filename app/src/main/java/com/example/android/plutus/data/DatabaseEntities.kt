package com.example.android.plutus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.plutus.CpiInflationRate
import com.example.android.plutus.RpiInflationRate

@Entity
data class DatabaseCpiInflationRate(
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
data class DatabaseRpiInflationRate(
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

fun List<DatabaseCpiInflationRate>.asCpiDomainModel(): List<CpiInflationRate> {
    return map {
        CpiInflationRate(
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

fun List<DatabaseRpiInflationRate>.asRpiDomainModel(): List<RpiInflationRate> {
    return map {
        RpiInflationRate(
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