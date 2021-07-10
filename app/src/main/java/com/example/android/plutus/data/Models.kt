package com.example.android.plutus

/**
 * These classes are for displaying data to the UI.
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

data class RpiPercentage(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

data class CpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

data class RpiItem(
    val date: String,
    val value: String,
    val label: String,
    val year: String,
    val month: String,
    val quarter: String,
    val sourceDataset: String,
    val updateDate: String)

data class GmpFixedRevaluation(
    val dateBegins: String,
    val dateEnds: String,
    val value: String)

data class Benefits (
    val pcls: String,
    val residualPension: String,
    val lta: String,
    val dcFund: String)

data class DateCalcResults (
    val years: Long,
    val months: Long,
    val weeks: Long,
    val days: Long,
    val yearsMonths: Pair<Long, Long>,
    val yearsDays: Pair<Long, Long>,
    val taxYears: Long,
    val sixthAprils: Long)

data class RevalResults (
    val cpiHigh: Double,
    val cpiLow: Double,
    val rpiHigh: Double,
    val rpiLow: Double,
    val gmpTaxYears: Double,
    val gmpSixthAprils: Double)

data class CalendarInfo(
    val year: Int,
    val month: Int,
    val day: Int)