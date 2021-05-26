@file:Suppress("DEPRECATION")

package com.example.android.plutus

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*

class CalcUtilsTest {

    @Test
    fun dbPclsCalculation_inputPensionNoDcFund_returnsExpectedPclsResidualLta() {
        // Create variables to enter into calculation
        val pension = 1000.00
        val cf = 20.00

        // Call the function
        val result = dbPclsCalculation(pension, cf)

        // Check the result
        assertThat(result.pcls, `is`("£5,000.00"))
        assertThat(result.residualPension,`is`("£750.00"))
        assertThat(result.dcFund, `is`("£0.00"))
        assertThat(result.lta, `is`("1.85%"))

    }

    @Test
    fun dbPclsCalculation_inputPensionWithDcFund_returnsExpectedPclsResidualDcFundLta() {
        // Create variables to enter into calculation
        val pension = 15614.23
        val cf = 12.32
        val dcFund = 3244.92

        // Call the function
        val result = dbPclsCalculation(pension, cf, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£67,544.70"))
        assertThat(result.residualPension, `is`("£10,131.71"))
        assertThat(result.dcFund, `is`("£3,244.92"))
        assertThat(result.lta, `is`("25.47%"))

    }

    @Test
    fun cmbPclsCalculation_inputPensioWithDcFund_returnsExpectedCombinedPclsResidualLta() {
        // Create variables to enter into calculation
        val pension = 28420.71
        val cf = 27.83
        val dcFund = 52917.34

        // Call the function
        val result = cmbPclsCalculation(pension, cf, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£163,081.59"))
        assertThat(result.residualPension, `is`("£24,462.24"))
        assertThat(result.dcFund, `is`("£0.00"))
        assertThat(result.lta, `is`("60.78%"))

    }

    @Test
    fun ltaCalculation_pclsResidualNoDcFund_returnsExpectedLta() {
        // Create variables to enter into calculation
        val pcls = 5000.00
        val residual = 750.00

        // Call the function
        val result = ltaCalculation(pcls, residual, 0.00)

        assertThat(result, `is`("1.85%"))
    }
}