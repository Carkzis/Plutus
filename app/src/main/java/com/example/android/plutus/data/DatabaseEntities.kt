package com.example.android.plutus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage

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

fun List<DatabaseCpiPct>.asCpiDomainModel(): List<CpiPercentage> {
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

fun List<DatabaseRpiPct>.asRpiDomainModel(): List<RpiPercentage> {
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