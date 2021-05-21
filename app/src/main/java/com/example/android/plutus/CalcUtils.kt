package com.example.android.plutus

import java.math.RoundingMode
import java.text.DecimalFormat

private const val STANDARD_LTA = 1073100

internal fun dbPclsCalculation(
    fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {
    // Calculate the pcls and the residual pension
    val pcls = (fullPension * commutationFactor * 20) / ((commutationFactor * 3) + 20)
    val commutedPension = pcls / commutationFactor
    val residual = fullPension - commutedPension
    val lta = ltaCalculation(pcls, residual, dcFund)

    return Benefits(pcls, residual, lta, dcFund)
}

internal fun cmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {
    // Calculate the pcls and residual when combined with the dc fund
    val pcls = dcFund + (commutationFactor * ((fullPension * 20) - (dcFund * 3)) /
            ((commutationFactor * 3) + 20))
    val commutedPension = (pcls - dcFund) / commutationFactor
    val residual = fullPension - commutedPension
    val lta = ltaCalculation(pcls, residual, dcFund)

    return Benefits(pcls, residual, lta, dcFund)
}

internal fun ltaCalculation(pcls: Double, residual: Double, dcFund: Double) : String {

    // Variable to format lifetime allowance to 2 decimal places (rounded down).
    val decimalFormat = DecimalFormat("#.##")
    decimalFormat.roundingMode = RoundingMode.DOWN

    // Calculate the individual LTAs, and convert them back into doubles after rounding.
    val pclsLta =
        decimalFormat.format((pcls / STANDARD_LTA) * 100).toDouble()
    val residualLta =
        decimalFormat.format((residual * 20) / STANDARD_LTA * 100).toDouble()
    val dcFundLta =
        decimalFormat.format((dcFund / STANDARD_LTA) * 100).toDouble()

    // Then add them together to give the total LTA (and convert back to a string).
    return "${(pclsLta + residualLta + dcFundLta)}%"

}

data class Benefits(
    val pcls: Double,
    val residualPension: Double,
    val lta: String,
    val dcFund: Double)