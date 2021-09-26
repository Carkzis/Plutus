package com.carkzis.android.plutus

/**
 * Model for displaying CPI 12-month percentages to the UI.
 */
data class CpiPercentage(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

/**
 * Model for displaying RPI 12-month percentages to the UI.
 */
data class RpiPercentage(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

/**
 * Model for displaying CPI items to the UI.
 */
data class CpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

/**
 * Model for displaying RPI items to the UI.
 */
data class RpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

/**
 * Holds the GMP fixed revaluation inputs.
 */
data class GmpFixedRevaluation(
    val dateBegins: String,
    val dateEnds: String,
    val value: String)

/**
 * Model for displaying pension calculation results to the UI.
 */
data class Benefits (
    val pcls: String,
    val residualPension: String,
    val lta: String,
    val dcFund: String)

/**
 * Model for displaying date calculation results (the duration between two dates
 * using different units of time) to the UI.
 */
data class DateCalcResults (
    val years: Long,
    val months: Long,
    val weeks: Long,
    val days: Long,
    val yearsMonths: Pair<Long, Long>,
    val yearsDays: Pair<Long, Long>,
    val taxYears: Long,
    val sixthAprils: Long)

/**
 * Model for displaying revaluation calculation results to the UI.
 */
data class RevalResults (
    var cpiHigh: Double,
    var cpiLow: Double,
    var rpiHigh: Double,
    var rpiLow: Double,
    val gmpTaxYears: Double,
    val gmpSixthAprils: Double)

/**
 * Holds calendar inputs from the Date Picker Dialogues.
 */
data class CalendarInfo(
    val year: Int,
    val month: Int,
    val day: Int)