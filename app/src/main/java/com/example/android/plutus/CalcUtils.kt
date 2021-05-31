package com.example.android.plutus

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

// Note: this is not actually a constant, but for initial purposes it is.
// In the future it can be changed to match the current UK amount.
private const val STANDARD_LTA = 1073100

internal fun dbPclsCalculation(
    fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {

    // Calculate the pcls and the residual pension
    val pcls = BigDecimal((fullPension * commutationFactor * 20) / ((commutationFactor * 3) + 20))
        .setScale(2, RoundingMode.HALF_EVEN)
    val commutedPension = BigDecimal(pcls.toDouble() / commutationFactor)
        .setScale(2, RoundingMode.HALF_EVEN)
    val residual = BigDecimal(fullPension - commutedPension.toDouble())
        .setScale(2, RoundingMode.HALF_EVEN)
    val dc = BigDecimal(dcFund)
        .setScale(2, RoundingMode.HALF_EVEN)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), dcFund)

    return Benefits(formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(dc))
}

internal fun cmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {

    // Calculate the pcls and residual when combined with the dc fund
    val pcls = BigDecimal(dcFund + ((commutationFactor * ((fullPension * 20) - (dcFund * 3))) /
            ((commutationFactor * 3) + 20))).setScale(2, RoundingMode.HALF_EVEN)
    val commutedPension = BigDecimal((pcls.toDouble() - dcFund) / commutationFactor)
        .setScale(2, RoundingMode.HALF_EVEN)
    val residual = BigDecimal(fullPension - commutedPension.toDouble())
        .setScale(2, RoundingMode.HALF_EVEN)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), 0.0)

    return Benefits(formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(
        BigDecimal.ZERO))
}

internal fun ltaCalculation(pcls: Double, residual: Double, dcFund: Double = 0.0) : String {

    // Calculate the individual LTAs, rounding them (down) to 2 dp
    val pclsLta =
        BigDecimal((pcls / STANDARD_LTA) * 100).setScale(2, RoundingMode.DOWN)
    val residualLta =
        BigDecimal((residual * 20) / STANDARD_LTA * 100).setScale(2, RoundingMode.DOWN)
    val dcFundLta =
        BigDecimal((dcFund / STANDARD_LTA) * 100).setScale(2, RoundingMode.DOWN)

    // Then add them together to give the total LTA (and convert back to a string).
    return "${(pclsLta + residualLta + dcFundLta)}%"

}

internal fun formatAsCurrency(value: BigDecimal) : String {
    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    return currencyFormat.format(value.toDouble())
}

data class Benefits(
    val pcls: String = "£0.00",
    val residualPension: String = "£0.00",
    val lta: String = "£0.00",
    val dcFund: String = "£0.00")

data class DateCalcResults(
    val years: String,
    val months: String,
    val weeks: String,
    val days: String,
    val yearsMonths: String,
    val yearsDays: String,
    val taxYears: String,
    val sixthAprils: String)

/**
 * Gets the number of days between start and end date, to ensure it is over or equal to 0.
 */
internal fun dateValidation(startDate: String, endDate: String) : Int {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    return Period.between(startDateObj, endDateObj).getDays()
}