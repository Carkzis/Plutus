package com.example.android.plutus

import java.math.RoundingMode
import java.text.DecimalFormat

// Class to store the pcls, residual pension and lta values to be returned when
// dbPclsCalculation() is called.
data class dbPclsAndResidual(val pcls: Double, val residualPension: Double, val lta: String)

private const val STANDARD_LTA = 1073100

fun dbPclsCalculation(fullPension: Double, commutationFactor: Int): dbPclsAndResidual {
    // Calculate the pcls and the residual pension
    val pcls = (fullPension * commutationFactor * 20) / ((commutationFactor * 3) + 20)
    val residual = fullPension - (pcls / commutationFactor)

    // Variable to format lifetime allowance to 2 decimal places (rounded down)
    val decimalFormat = DecimalFormat("#.##")
    decimalFormat.roundingMode = RoundingMode.DOWN

    // Calculate the individual LTAs, and convert them back into doubles after rounding
    // then add them together to give the total LTA (and back to a string)
    val pclsLta = decimalFormat.format((pcls / STANDARD_LTA) * 100).toDouble()
    val residualLta = decimalFormat.format((residual * 20) / STANDARD_LTA * 100).toDouble()
    val totalLta = "${(pclsLta + residualLta)}%"

    return dbPclsAndResidual(pcls, residual, totalLta)
}