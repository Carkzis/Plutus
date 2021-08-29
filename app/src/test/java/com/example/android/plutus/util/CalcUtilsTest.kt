@file:Suppress("DEPRECATION")

package com.example.android.plutus.util

import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.number.OrderingComparison.lessThan
import org.junit.Test

import org.junit.Assert.*

class CalcUtilsTest {

    // TODO: Test different standard Lta entries.

    @Test
    fun dbPclsCalculation_inputPensionNoDcFund_returnsExpectedPclsResidualLta() {
        // Create variables to enter into calculation
        val pension = 1000.00
        val cf = 20.00
        val sLta = 1073100.00

        // Call the function
        val result = dbPclsCalculation(pension, cf, sLta)

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
        val sLta = 1073100.00

        // Call the function
        val result = dbPclsCalculation(pension, cf, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£67,544.70"))
        assertThat(result.residualPension, `is`("£10,131.71"))
        assertThat(result.dcFund, `is`("£3,244.92"))
        assertThat(result.lta, `is`("25.47%"))

    }

    @Test
    fun cmbPclsCalculation_inputPensionWithDcFund_returnsExpectedCombinedPclsResidualLta() {
        // Create variables to enter into calculation
        val pension = 28420.71
        val cf = 27.83
        val dcFund = 52917.34
        val sLta = 1073100.00

        // Call the function
        val result = smallCmbPclsCalculation(pension, cf, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£163,081.59"))
        assertThat(result.residualPension, `is`("£24,462.24"))
        assertThat(result.dcFund, `is`("£0.00"))
        assertThat(result.lta, `is`("60.78%"))

    }

    @Test
    fun largePclsCalculation_inputWithDcEqualToMaxAndDcForAnnuity_returnsZeroForDc() {
        // Create variables to enter into calculation
        val pension = 30000.00
        val cf = 15.00
        val dcFund = 200000.00
        val residualDcForAnnuity = true
        val sLta = 1073100.00

        // Call the function
        val result = largeCmbPclsCalculation(pension, cf, residualDcForAnnuity, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£200,000.00"))
        assertThat(result.residualPension, `is`("£30,000.00"))
        assertThat(result.dcFund, `is`("£0.00"))
        assertThat(result.lta, `is`("74.54%"))

    }

    @Test
    fun largeCmbPclsCalculation_inputWithDcEqualToMaxAndDcForUfpls_returnsZeroForDc() {
        // Create variables to enter into calculation
        val pension = 30000.00
        val cf = 15.00
        val dcFund = 200000.00
        val residualDcForAnnuity = false
        val sLta = 1073100.00

        // Call the function
        val result = largeCmbPclsCalculation(pension, cf, residualDcForAnnuity, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£200,000.00"))
        assertThat(result.residualPension, `is`("£30,000.00"))
        assertThat(result.dcFund, `is`("£0.00"))
        assertThat(result.lta, `is`("74.54%"))

    }

    @Test
    fun largeCmbPclsCalculation_inputLargeDcFundForAnnuity_returnsExpectedCmbPclsResidualDcLta() {

        // Create variables to enter into calculation
        val pension = 14234.01
        val cf = 21.32
        val dcFund = 425232.46
        val residualDcForAnnuity = true
        val sLta = 1073100.00

        // Call the function
        val result = largeCmbPclsCalculation(pension, cf, residualDcForAnnuity, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£177,478.17"))
        assertThat(result.residualPension, `is`("£14,234.01"))
        assertThat(result.dcFund, `is`("£247,754.29"))
        assertThat(result.lta, `is`("66.13%"))

    }

    @Test
    fun largeCmbPclsCalculation_inputLargeDcFundForUfpls_returnsExpectedCmbPclsResidualDcLta() {

        // Create variables to enter into calculation
        val pension = 14234.01
        val cf = 21.32
        val dcFund = 425232.46
        val residualDcForAnnuity = false
        val sLta = 1073100.00

        // Call the function
        val result = largeCmbPclsCalculation(pension, cf, residualDcForAnnuity, sLta, dcFund)

        // Check the results
        assertThat(result.pcls, `is`("£94,893.40"))
        assertThat(result.residualPension, `is`("£14,234.01"))
        assertThat(result.dcFund, `is`("£330,339.06"))
        assertThat(result.lta, `is`("66.14%"))

    }

    @Test
    fun ltaCalculation_pclsResidualNoDcFund_returnsExpectedLta() {
        // Create variables to enter into calculation
        val pcls = 5000.00
        val residual = 750.00
        val sLta = 1073100.00

        // Call the function
        val result = ltaCalculation(pcls, residual, 0.00, sLta)

        assertThat(result, `is`("1.85%"))
    }

    @Test
    fun ltaCalculation_noPcls_returnsExpectedLta() {
        // Create variables to enter into calculation
        val fullPension = 1000.00
        val sLta = 1073100.00

        // Call the function
        val result = ltaCalculation(0.0, fullPension, 0.00, sLta)

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

    @Test
    fun fixedGmpCalculation_passSixthAprilButTestFullTaxYears_returns1point0() {
        val startDate = "05/03/2021"
        val endDate = "07/08/2021"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.0))
    }

    @Test
    fun fixedGmpCalculation_passSixthAprilAndTestSixthAprils_returns1point035() {
        val startDate = "05/03/2021"
        val endDate = "07/08/2021"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, false)

        assertThat(gmpRevaluation, `is`(1.035))
    }

    @Test
    fun fixedGmpCalculation_5April1988to7August2021_returns1point035() {
        val startDate = "05/04/1988"
        val endDate = "07/08/2021"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(14.7632))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween1988and1993_returns1point075() {
        val startDate = "06/04/1988"
        val endDate = "06/04/1989"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.075))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween1993and1997_returns1point07() {
        val startDate = "06/04/1995"
        val endDate = "06/04/1996"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.07))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween1997and2002_returns1point0625() {
        val startDate = "06/04/2000"
        val endDate = "06/04/2001"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.0625))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween2002and2007_returns1point045() {
        val startDate = "06/04/2004"
        val endDate = "06/04/2005"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.045))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween2007and2012_returns1point04() {
        val startDate = "06/04/2007"
        val endDate = "06/04/2008"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.04))
    }

    @Test
    fun fixedGmpCalculation_oneTaxYearBetween2012and2017_returns1point0475() {
        val startDate = "06/04/2015"
        val endDate = "06/04/2016"

        // Call the function
        val gmpRevaluation = gmpRevaluationCalculation(startDate, endDate, true)

        assertThat(gmpRevaluation, `is`(1.0475))
    }

    @Test
    fun checkRevalDates_startDateBeforeNoRevalDate_returnsDummyDate() {
        // Note: any start date prior to 01/01/1986 should mean no revaluation is applied
        val startDate = "31/12/1985"

        val checkString = checkRevalDates(startDate, 2.5)

        assertThat(checkString, `is`("01/01/1900"))
    }

    @Test
    fun checkRevalDates_startDateBefore2009ButCap5_returnsStartDate() {
        // Note: any start date prior to 01/01/1986 should mean no revaluation is applied
        val startDate = "01/01/2005"
        val cap = 5

        val checkString = checkRevalDates(startDate, 5.0)

        assertThat(checkString, `is`("01/01/2005"))
    }

    @Test
    fun checkRevalDates_startDateBefore2009ButCap2point5_returns2009Date() {
        // Note: any start date prior to 01/01/1986 should mean no revaluation is applied
        val startDate = "01/01/2005"
        val cap = 5

        val checkString = checkRevalDates(startDate, 2.5)

        assertThat(checkString, `is`("06/04/2009"))
    }

    @Test
    fun checkRevalDates_startDateAfter2009ButCap2point5_returnsStartDate() {
        // Note: any start date prior to 01/01/1986 should mean no revaluation is applied
        val startDate = "01/01/2015"
        val cap = 5

        val checkString = checkRevalDates(startDate, 2.5)

        assertThat(checkString, `is`("01/01/2015"))
    }

    val rpiRevList = mutableListOf<RpiPercentage> (
        RpiPercentage("", "3.9%", "", "2007", "", "", "", ""),
        RpiPercentage("", "5.0%", "", "2008", "", "", "", ""),
        RpiPercentage("", "-1.4%", "", "2009", "", "", "", ""),
        RpiPercentage("", "4.6%", "", "2010", "", "", "", ""),
        RpiPercentage("", "5.6%", "", "2011", "", "", "", ""),
        RpiPercentage("", "2.6%", "", "2012", "", "", "", ""),
        RpiPercentage("", "3.2%", "", "2013", "", "", "", ""),
    )

    val cpiRevList = mutableListOf<CpiPercentage> (
        CpiPercentage("", "1.8%", "", "2007", "", "", "", ""),
        CpiPercentage("", "5.2%", "", "2008", "", "", "", ""),
        CpiPercentage("", "1.1%", "", "2009", "", "", "", ""),
        CpiPercentage("", "3.1%", "", "2010", "", "", "", ""),
        CpiPercentage("", "5.2%", "", "2011", "", "", "", ""),
        CpiPercentage("", "2.2%", "", "2012", "", "", "", ""),
        CpiPercentage("", "2.7%", "", "2013", "", "", "", ""),
    )

    @Test
    fun revaluationCalculation_noFutureRevaluation_returnMinusOne() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2012"
        val endDate = "06/04/2015"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(-1.0))
    }

    @Test
    fun revaluationCalculation_minusRpiForYear_returnOne() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2009"
        val endDate = "06/04/2010"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.0))
    }

    @Test
    fun revaluationCalculation_rpiCap5RevaluationOverAvailablePeriod_returnCorrectRate() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2008"
        val endDate = "06/04/2014"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.211))
    }

    @Test
    fun revaluationCalculation_rpiCap2point5RevaluationOverAvailablePeriod_returnCorrectRate() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2008"
        val endDate = "06/04/2014"
        val cap = 2.5

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.131))
    }

    @Test
    fun revaluationCalculation_cpiCap5RevaluationOverAvailablePeriod_returnCorrectRate() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2008"
        val endDate = "06/04/2014"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, false)

        assertThat(calcResult, `is`(1.179))
    }

    @Test
    fun revaluationCalculation_cpiCap2point5RevaluationOverAvailablePeriod_returnCorrectRate() {
        // Note: the latest year we should have is for 2014.
        val startDate = "06/04/2008"
        val endDate = "06/04/2014"
        val cap = 2.5

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, false)

        assertThat(calcResult, `is`(1.122))
    }

    @Test
    fun revaluationCalculation_pre1986StartDate_returnOne() {

        // Need small lists with pre 1986 dates.
        val rpiRevList = mutableListOf<RpiPercentage> (
            RpiPercentage("", "", "", "1980", "", "", "", ""),
            RpiPercentage("", "", "", "1981", "", "", "", ""),
        )
        val cpiRevList = mutableListOf<CpiPercentage> (
            CpiPercentage("", "", "", "1980", "", "", "", ""),
            CpiPercentage("", "", "", "1981", "", "", "", ""),
        )
        val startDate = "06/04/1981"
        val endDate = "06/04/1982"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.0))
    }

    @Test
    fun revaluationCalculation_rpi1986Year_returnConvertedFigure3point1() {
        // Need small lists with pre 1986 dates.
        val rpiRevList = mutableListOf<RpiPercentage> (
            RpiPercentage("", "1.0", "", "1986", "", "", "", ""),
            RpiPercentage("", "1.0", "", "1987", "", "", "", ""),
        )
        val cpiRevList = mutableListOf<CpiPercentage> (
            CpiPercentage("", "1.0", "", "1986", "", "", "", ""),
            CpiPercentage("", "1.0", "", "1987", "", "", "", ""),
        )
        val startDate = "06/04/1986"
        val endDate = "06/04/1987"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.031))
    }

    @Test
    fun revaluationCalculation_rpi1988Year_returnConvertedFigureCappedAt5() {
        // Need small lists with pre 1986 dates.
        val rpiRevList = mutableListOf<RpiPercentage> (
            RpiPercentage("", "1.0", "", "1988", "", "", "", ""),
            RpiPercentage("", "1.0", "", "1989", "", "", "", ""),
        )
        val cpiRevList = mutableListOf<CpiPercentage> (
            CpiPercentage("", "1.0", "", "1988", "", "", "", ""),
            CpiPercentage("", "1.0", "", "1989", "", "", "", ""),
        )
        val startDate = "06/04/1988"
        val endDate = "06/04/1989"
        val cap = 5.0

        // Test the function in question
        val calcResult = revaluationCalculation(startDate, endDate, cpiRevList,
            rpiRevList, cap, true)

        assertThat(calcResult, `is`(1.05))
    }

}