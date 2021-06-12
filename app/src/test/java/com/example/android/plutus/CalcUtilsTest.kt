@file:Suppress("DEPRECATION")

package com.example.android.plutus

import com.example.android.plutus.util.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.number.OrderingComparison.lessThan
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

    @Test
    fun ltaCalculation_noPcls_returnsExpectedLta() {
        // Create variables to enter into calculation
        val fullPension = 1000.00

        // Call the function
        val result = ltaCalculation(0.0, fullPension, 0.00)

        assertThat(result, `is`("1.86%"))
    }

    @Test
    fun daysCalculation_sameDateUsed_returnsOne() {
        // Create variables to enter into calculation
        val startDate = "31/05/2021"
        val endDate = "31/05/2021"

        // Call the function
        val result = daysCalculation(startDate, endDate)

        assertThat(result, `is`(1))
    }

    @Test
    fun daysCalculation_endDateBeforeStartDate_returnsNegativeNumber() {
        // Create variables to enter into calculation
        val startDate = "31/05/2021"
        val endDate = "31/05/2020"

        // Call the function
        val result = daysCalculation(startDate, endDate)

        assertThat(result, `is`(lessThan(0)))
    }

    @Test
    fun daysCalculation_startDateAfterEndDate_returnsPositiveNumber() {
        // Create variables to enter into calculation
        val startDate = "31/05/2021"
        val endDate = "30/05/2022"

        // Call the function
        val result = daysCalculation(startDate, endDate)

        assertThat(result, `is`(365))
    }

    @Test
    fun daysCalculation_periodOfOneMonth_returnsAllDaysInMonth() {
        // Create variables to enter into calculation
        val startDate = "01/05/2021"
        val endDate = "31/05/2021"

        // Call the function
        val result = daysCalculation(startDate, endDate)

        assertThat(result, `is`(31))
    }

    @Test
    fun yearsCalculation_periodOfOneYear_returnsOneYear() {
        // Create variables to enter into calculation
        val startDate = "01/05/2021"
        val endDate = "30/04/2022"

        // Call the function
        val result = yearsCalculation(startDate, endDate)

        assertThat(result, `is`(1))
    }

    @Test
    fun yearsCalculation_periodOfMultipleYears_returnsSeven() {
        val startDate = "15/09/1993"
        val endDate = "04/05/2001"

        // Call the function
        val result = yearsCalculation(startDate, endDate)

        assertThat(result, `is`(7))
    }

    @Test
    fun yearsCalculation_periodLessThanOneYear_returnsZero() {
        val startDate = "15/03/2011"
        val endDate = "13/03/2012"

        // Call the function
        val result = yearsCalculation(startDate, endDate)

        assertThat(result, `is`(0))
    }

    @Test
    fun yearsCalculation_periodLessThanOneYearLeapYear_returnsZero() {
        val startDate = "01/03/2019"
        val endDate = "28/02/2020" // Last day is 29/02/2020.

        // Call the function
        val result = yearsCalculation(startDate, endDate)

        assertThat(result, `is`(0))
    }

    @Test
    fun monthsCalculation_periodOfOneMonth_returnsOne() {
        val startDate = "01/04/2014"
        val endDate = "30/04/2014"

        // Call the function
        val result = monthsCalculation(startDate, endDate)

        assertThat(result, `is`(1))
    }

    @Test
    fun monthsCalculation_periodOfManyMonths_returnsSeven() {
        val startDate = "13/07/2016"
        val endDate = "02/03/2017"

        // Call the function
        val result = monthsCalculation(startDate, endDate)

        assertThat(result, `is`(7))
    }

    @Test
    fun monthsCalculation_periodLessThanOneMonth_returnsZero() {
        val startDate = "25/12/2012"
        val endDate = "23/01/2013"

        // Call the function
        val result = monthsCalculation(startDate, endDate)

        assertThat(result, `is`(0))
    }

    @Test
    fun weeksCalculation_periodOfWeekMonth_returnsOne() {
        val startDate = "17/11/2014"
        val endDate = "25/11/2014"

        // Call the function
        val result = weeksCalculation(startDate, endDate)

        assertThat(result, `is`(1))
    }

    @Test
    fun weeksCalculation_periodOfManyWeeks_returnsSeven() {
        val startDate = "18/08/1993"
        val endDate = "09/10/1993"

        // Call the function
        val result = weeksCalculation(startDate, endDate)

        assertThat(result, `is`(7))
    }

    @Test
    fun weeksCalculation_periodLessThanOneWeek_returnsZero() {
        val startDate = "18/03/2000"
        val endDate = "22/03/2000"

        // Call the function
        val result = weeksCalculation(startDate, endDate)

        assertThat(result, `is`(0))
    }

    @Test
    fun yearsAndMonthsCalculation_periodLessThanOneYear_returnsZeroYearsSevenMonths() {
        val startDate = "13/07/2016"
        val endDate = "02/03/2017"

        // Call the function
        val (years, months) = yearsAndMonthsCalculation(startDate, endDate)

        assertThat(years, `is`(0))
        assertThat(months, `is`(7))
    }

    @Test
    fun yearsAndMonthsCalculation_periodExactlyOneYearNoMonths_returnsOneYearZeroMonths() {
        val startDate = "12/12/2012"
        val endDate = "11/12/2013"

        // Call the function
        val (years, months) = yearsAndMonthsCalculation(startDate, endDate)

        assertThat(years, `is`(1))
        assertThat(months, `is`(0))
    }

    @Test
    fun yearsAndMonthsCalculation_periodOneYearExactlyOneMonth_returnsOneYearOneMonths() {
        val startDate = "12/12/2012"
        val endDate = "11/01/2014"

        // Call the function
        val (years, months) = yearsAndMonthsCalculation(startDate, endDate)

        assertThat(years, `is`(1))
        assertThat(months, `is`(1))
    }

    @Test
    fun yearsAndMonthsCalculation_periodManysYearsManyMonths_returnsFortyTwoYearsSevenMonths() {
        val startDate = "04/02/1942"
        val endDate = "17/09/1984"

        // Call the function
        val (years, months) = yearsAndMonthsCalculation(startDate, endDate)

        assertThat(years, `is`(42))
        assertThat(months, `is`(7))
    }

    @Test
    fun yearsAndDaysCalculation_periodLessThanOneYears_returnsZeroYearsManyDays() {
        val startDate = "20/06/1983"
        val endDate = "04/12/1983"

        // Call the function
        val (years, days) = yearsAndDaysCalculation(startDate, endDate)

        assertThat(years, `is`(0))
        assertThat(days, `is`(168))
    }

    @Test
    fun yearsAndDaysCalculation_periodExactlyOneYear_returnsOneYearZeroDays() {
        val startDate = "15/08/2007"
        val endDate = "14/08/2008"

        // Call the function
        val (years, days) = yearsAndDaysCalculation(startDate, endDate)

        assertThat(years, `is`(1))
        assertThat(days, `is`(0))
    }

    @Test
    fun yearsAndDaysCalculation_periodExactlyOneYearOneDay_returnsOneYearOneDay() {
        val startDate = "15/08/2007"
        val endDate = "15/08/2008"

        // Call the function
        val (years, days) = yearsAndDaysCalculation(startDate, endDate)

        assertThat(years, `is`(1))
        assertThat(days, `is`(1))
    }

    @Test
    fun yearsAndDaysCalculation_periodManyYearsManyDays_returnsSevenYearsSeventySevenDays() {
        val startDate = "19/04/2014"
        val endDate = "04/07/2021"

        // Call the function
        val (years, days) = yearsAndDaysCalculation(startDate, endDate)

        assertThat(years, `is`(7))
        assertThat(days, `is`(77))
    }

    @Test
    fun taxYearsCalculation_periodExactlyOneTaxYear_returnsOneTaxYear() {
        val startDate = "06/04/2021"
        val endDate = "05/04/2022"

        // Call the function
        val taxYears = taxYearsCalculation(startDate, endDate)

        assertThat(taxYears, `is`(1))
    }

    @Test
    fun taxYearsCalculation_periodLessThanOneTaxYear_returnsZeroTaxYears() {
        val startDate = "06/06/2021"
        val endDate = "03/03/2022"

        // Call the function
        val taxYears = taxYearsCalculation(startDate, endDate)

        assertThat(taxYears, `is`(0))
    }

    @Test
    fun taxYearsCalculation_periodManyYears_returnsSevenTaxYears() {
        val startDate = "27/01/2013"
        val endDate = "19/09/2020"

        // Call the function
        val taxYears = taxYearsCalculation(startDate, endDate)

        assertThat(taxYears, `is`(7))
    }

    @Test
    fun taxYearsCalculation_oneYearAfterOneYearBeforeTaxYearEnd_returnsTen() {
        val startDate = "03/11/1994"
        val endDate = "11/03/2006"

        // Call the function
        val taxYears = taxYearsCalculation(startDate, endDate)

        assertThat(taxYears, `is`(10))
    }

    @Test
    fun sixthAprilsPassCalculation_lessThanOneTaxYearPassSixthApril_returnsOne() {
        val startDate = "03/11/1994"
        val endDate = "11/04/1995"

        // Call the function
        val aprilsPassed = sixthAprilsPassCalculation(startDate, endDate)

        assertThat(aprilsPassed, `is`(1))
    }

    @Test
    fun sixthAprilsPassCalculation_periodExactlyOneDaySixthApril_returnsOne() {
        val startDate = "06/04/1994"
        val endDate = "06/04/1994"

        // Call the function
        val aprilsPassed = sixthAprilsPassCalculation(startDate, endDate)

        assertThat(aprilsPassed, `is`(1))
    }

    @Test
    fun sixthAprilsPassCalculation_periodExactlyOneTaxYear_returnsOne() {
        val startDate = "06/04/1994"
        val endDate = "05/04/1995"

        // Call the function
        val aprilsPassed = sixthAprilsPassCalculation(startDate, endDate)

        assertThat(aprilsPassed, `is`(1))
    }

    @Test
    fun sixthAprilsPassCalculation_periodOneTaxYearOneDay_returnsTwo() {
        val startDate = "06/04/1994"
        val endDate = "06/04/1995"

        // Call the function
        val aprilsPassed = sixthAprilsPassCalculation(startDate, endDate)

        assertThat(aprilsPassed, `is`(2))
    }

    @Test
    fun sixthAprilsPassCalculation_periodManyYears_returnsSeven() {
        val startDate = "05/04/1994"
        val endDate = "09/09/2000"

        // Call the function
        val aprilsPassed = sixthAprilsPassCalculation(startDate, endDate)

        assertThat(aprilsPassed, `is`(7))
    }


}