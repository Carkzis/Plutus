package com.example.android.plutus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseCpiInflationRate(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    @PrimaryKey
    val updateDate: String)

fun List<DatabaseCpiInflationRate>.asDomainModel(): List<InflationRate> {
    return map {
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