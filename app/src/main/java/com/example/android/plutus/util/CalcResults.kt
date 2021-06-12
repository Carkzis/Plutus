package com.example.android.plutus.util

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