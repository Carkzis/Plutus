package com.example.android.plutus

/**
 * These classes are for displaying data to the UI.
 */

data class InflationRate(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)