@file:Suppress("DEPRECATION")

package com.example.android.plutus

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*

class CalcUtilsTest {

    @Test
    fun dbPclsCalculation_inputPensionNoDcFund_returnsExpectedPclsResidualLta() {
        // Create variable to enter into calculation
        val pension: Double = 1000.00
        val cf: Double = 20.00

        // Call the function
        val result = dbPclsCalculation(pension, cf)

        // Check the result
        assertThat(result.pcls, `is`(5000.00))
    }

    @Test
    fun cmbPclsCalculation() {
    }

    @Test
    fun ltaCalculation() {
    }
}