package com.carkzis.android.plutus.util

import com.carkzis.android.plutus.Benefits
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.RpiPercentage
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Calculates the defined benefits pension commencement lump sum, and returns the benefits.
 * PCLS calculation: (P * CF * 20) / ((CF * 3) + 20)
 * Residual calculation: P - (PCLS / CF)
 * Lifetime Allowance (LTA): (PCLS / sLTA) + ((P * 20) / sLTA) + (DC / sLTA) - all 2dp rounded down
 * P = Pension, CF = Commutation Factor, PCLS = Pension Commencement Lump Sum,
 * sLTA = Standard Lifetime Allowance, DC = Defined Contribution Fund
 */
internal fun dbPclsCalculation(
    fullPension: Double, commutationFactor: Double, sLta: Double, dcFund: Double = 0.0): Benefits {

    // Calculate the pcls and the residual pension.
    val pcls = BigDecimal((fullPension * commutationFactor * 20) / ((commutationFactor * 3) + 20))
        .setScale(2, RoundingMode.HALF_UP)
    val commutedPension = BigDecimal(pcls.toDouble() / commutationFactor)
        .setScale(2, RoundingMode.HALF_UP)
    val residual = BigDecimal(fullPension - commutedPension.toDouble())
        .setScale(2, RoundingMode.HALF_UP)
    val dc = BigDecimal(dcFund)
        .setScale(2, RoundingMode.HALF_UP)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), dcFund, sLta)

    return Benefits(formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(dc))
}

/**
 * Calculates the combined pension commencement lump sum, where all the DC fund can be taken
 * as a tax free lump sum, and returns the benefits.
 * PCLS calculation: ((CF * ((P * 20) - (DC * 3)) / ((CF * 3) + 20)) + DC
 * Residual calculation: P - ((PCLS - DC) / CF)
 * Lifetime Allowance (LTA): (PCLS / sLTA) + ((P * 20) / sLTA) - all 2dp rounded down
 * P = Pension, CF = Commutation Factor, PCLS = Pension Commencement Lump Sum,
 * sLTA = Standard Lifetime Allowance, DC = Defined Contribution Fund
 */
internal fun smallCmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, sLta: Double, dcFund: Double = 0.0): Benefits {

    // Calculate the pcls and residual when combined with the dc fund.
    val pcls = BigDecimal(dcFund + ((commutationFactor * ((fullPension * 20) - (dcFund * 3))) /
            ((commutationFactor * 3) + 20))).setScale(2, RoundingMode.HALF_UP)
    val commutedPension = BigDecimal((pcls.toDouble() - dcFund) / commutationFactor)
        .setScale(2, RoundingMode.HALF_UP)
    val residual = BigDecimal(fullPension - commutedPension.toDouble())
        .setScale(2, RoundingMode.HALF_UP)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), 0.0, sLta)

    return Benefits(
        formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(
        BigDecimal.ZERO)
    )
}

/**
 * Calculates the combined pension commencement lump sum, where only sum of the DC fund can
 * be taken as a tax free lump sum, and returns the benefits. The calculation is different
 * if the residual defined contribution fun is used to take an annuity.
 * PCLS calculation (if annuity taken): ((P * 20) + DC) / 4
 * PCLS calculation (if no annuity taken): ((P * 20) / 3)
 * Residual calculation: P (doesn't change)
 * Lifetime Allowance (LTA): (PCLS / sLTA) + ((P * 20) / sLTA) + (DC / sLTA) - all 2dp rounded down
 * P = Pension, CF = Commutation Factor, PCLS = Pension Commencement Lump Sum,
 * sLTA = Standard Lifetime Allowance, DC = Defined Contribution Fund
 */
internal fun largeCmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, residualDcForAnnuity: Boolean, sLta: Double,
    dcFund: Double = 0.0): Benefits {

    // This is the largest amount of DC that can be combined with the DB benefits to make a PCLS.
    val maxDcToCombine = BigDecimal((fullPension * 20) / 3)
        .setScale(2, RoundingMode.HALF_UP)

    /*
     PCLS is calculated differently depending on if DC is used to buy an annuity
     or taken as a UFPLS.
     */
    val pcls = if (residualDcForAnnuity) BigDecimal(((fullPension * 20) + dcFund) / 4)
        .setScale(2, RoundingMode.HALF_UP) else maxDcToCombine
    // Residual will be the same as the full pension with a large lump sum.
    val residual = BigDecimal(fullPension)
        .setScale(2, RoundingMode.HALF_UP)
    val dc = BigDecimal(dcFund - pcls.toDouble())
        .setScale(2, RoundingMode.HALF_UP)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), dc.toDouble(), sLta)

    return Benefits(
        formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(dc)
    )
}

/**
 * Calculates the lifetime allowance (LTA).
 * Lifetime Allowance (LTA): (PCLS / sLTA) + ((P * 20) / sLTA) + (DC / sLTA)
 *  - all 2dp rounded down
 * P = Pension, CF = Commutation Factor, PCLS = Pension Commencement Lump Sum,
 * sLTA = Standard Lifetime Allowance, DC = Defined Contribution Fund
 */
internal fun ltaCalculation(pcls: Double, pension: Double, dcFund: Double = 0.0, sLta: Double) : String {
    // Calculate the individual LTAs, rounding them (down) to 2 dp.
    val pclsLta =
        BigDecimal((pcls / sLta) * 100).setScale(2, RoundingMode.DOWN)
    val pensionLta =
        BigDecimal((pension * 20) / sLta * 100).setScale(2, RoundingMode.DOWN)
    val dcFundLta =
        BigDecimal((dcFund / sLta) * 100).setScale(2, RoundingMode.DOWN)

    // Then add them together to give the total LTA (and convert back to a string).
    return "${(pclsLta + pensionLta + dcFundLta)}%"
}

/**
 * Formats the data as currency.
 */
internal fun formatAsCurrency(value: BigDecimal) : String {
    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    return currencyFormat.format(value.toDouble())
}

/**
 * Calculates the number of days between two dates. The last day is included, as is standard
 * in pensions calculations.
 */
internal fun daysCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    // Last day is included in days calculation.
    return ChronoUnit.DAYS.between(startDateObj, endDateObj)
}

/**
 * Calculates the number of years between two dates. The last day is included, as is standard
 * in pensions calculations.
 */
internal fun yearsCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.YEARS.between(startDateObj, endDateObj)
}

/**
 * Calculates the number of months between two dates. The last day is included, as is standard
 * in pensions calculations.
 */
internal fun monthsCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.MONTHS.between(startDateObj, endDateObj)
}

/**
 * Calculates the number of weeks between two dates. The last day is included, as is standard
 * in pensions calculations.
 */
internal fun weeksCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.WEEKS.between(startDateObj, endDateObj)
}

/**
 * Calculates the number of years and months between two dates, returning a pair of (Year, Month).
 * The last day is included, as is standard in pensions calculations.
 */
internal fun yearsAndMonthsCalculation(startDate: String, endDate: String) : Pair<Long, Long> {
    val totalMonths = monthsCalculation(startDate, endDate)
    val years = totalMonths / 12
    val months = totalMonths % 12
    return Pair(years, months)
}

/**
 * Calculates the number of years and days between two dates, returning a pair of (Year, Month).
 * The last day is included, as is standard in pensions calculations.
 */
internal fun yearsAndDaysCalculation(startDate: String, endDate: String) : Pair<Long, Long> {
    val completeYears = yearsCalculation(startDate, endDate)
    if (completeYears == 0L) return Pair(0, daysCalculation(startDate, endDate))
    // This will account for leap years.
    val startDateForDays = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusYears(completeYears)
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    // Last day is included in days calculation.
    val days = ChronoUnit.DAYS.between(startDateForDays, endDateObj) + 1
    return Pair(completeYears, days)
}

/**
 * Calculates the amount of full tax years between two dates, a full tax year being
 * 06/04/YYYY to 05/04/YYYY+1.
 */
internal fun taxYearsCalculation(startDate: String, endDate: String) : Long {
    /* Check if start date is before 06/04/XXXX, if so just change day and month to
    change to 06/04/XXXX, otherwise change it to 06/04/XXXX+1.
    Do reverse for end date.
     */
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    val startDateYearsTaxYearStart = LocalDate.parse("06/04/${startDateObj.year}",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateYearsTaxYearEnd = LocalDate.parse("05/04/${endDateObj.year}",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)

    // Choose which is the first tax year start date after the input start date.
    val taxYearStartDate =
        if (ChronoUnit.DAYS.between(startDateObj, startDateYearsTaxYearStart) >= 0)  {
            startDateYearsTaxYearStart
        } else {
            LocalDate.parse("06/04/${startDateObj.year + 1}",
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

    // Choose which is the first tax year end date before the input end date.
    val taxYearEndDate =
        if (ChronoUnit.DAYS.between(endDateObj, endDateYearsTaxYearEnd) <= 0) {
            endDateYearsTaxYearEnd
        } else {
            // Note: tax year ends 05/04 of each year, but add one day for whole year calc purposes.
            LocalDate.parse("05/04/${endDateObj.year - 1}",
                DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
        }

    /*
    Cannot be less than 0, which will occur if the start date is after 6 April in one year,
    and the end date is before 5 April in the following year
     */
    return maxOf(ChronoUnit.YEARS.between(taxYearStartDate, taxYearEndDate), 0)
}

/**
 * Calculates the number of sixth of Aprils pass between two dates.
 */
internal fun sixthAprilsPassCalculation(startDate: String, endDate: String) : Long {

    // Get the number of days to iterate through.
    val numberOfDays = daysCalculation(startDate, endDate)

    // Get the start date as a LocalDate object.
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    var currentDateObj = startDateObj
    var sixthAprils = 0

    // Iterate through the days, and increment the amount of sixth Aprils on matches.
    (0 until numberOfDays).forEach { _ ->
        if (currentDateObj.monthValue == 4 && currentDateObj.dayOfMonth == 6) {
            sixthAprils += 1
        }
        currentDateObj = currentDateObj.plusDays(1)
    }

    return sixthAprils.toLong()
}

/**
 * Calculates the GMP revaluation between two dates.  It is (1 + (Revaluation Rate / 100)) ^ Years.
 * The revaluation rate depends on the leaving date used.
 */
internal fun gmpRevaluationCalculation(startDate: String, endDate: String,
                                       isFullTaxYears: Boolean) : Double {

    val gmpStartDate = LocalDate.parse(
        "06/04/1978",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val validatedStartDate: String

    // GMP started on 06/04/1978, so limited the start date to this.
    if (startDateObj.isBefore(gmpStartDate)) {
        validatedStartDate = "06/04/1978"
    } else {
        validatedStartDate = startDate
    }

    // Get the number of years to revalue by depending on the isFullTaxYears toggle.
    val years = if (isFullTaxYears) taxYearsCalculation(validatedStartDate, endDate)
    else sixthAprilsPassCalculation(validatedStartDate, endDate)

    // Create LocalDate objects for all the tranches of GMP revaluation.
    val gmpDate1988 = LocalDate.parse("06/04/1988",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate1993 = LocalDate.parse("06/04/1993",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate1997 = LocalDate.parse("06/04/1997",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate2002 = LocalDate.parse("06/04/2002",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate2007 = LocalDate.parse("06/04/2007",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate2012 = LocalDate.parse("06/04/2012",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val gmpDate2017 = LocalDate.parse("06/04/2017",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    // Get different revaluation rates depending on the start date for revaluation.
    val fixedGmpRate = when {
        startDateObj.isBefore(gmpDate1988) -> 1.085
        startDateObj.isBefore(gmpDate1993) -> 1.075
        startDateObj.isBefore(gmpDate1997) -> 1.070
        startDateObj.isBefore(gmpDate2002) -> 1.0625
        startDateObj.isBefore(gmpDate2007) -> 1.045
        startDateObj.isBefore(gmpDate2012) -> 1.04
        startDateObj.isBefore(gmpDate2017) -> 1.0475
        else -> 1.035
    }

    // Need to get the figure to 3 decimal places.
    val formattedResult = BigDecimal(fixedGmpRate.pow(years.toDouble()))
        .setScale(4, RoundingMode.HALF_UP)

    return formattedResult.toDouble()
}

/**
 * This checks that the revaluation dates, returning at least 06/04/2009 if the cap is 2.5%,
 * returning "01/01/1900" if there is no revaluation (an error string), or
 * the date input otherwise.
 */
internal fun checkRevalDates(startDate: String, cap: Double): String {

    /*
     If the cap is 2.5%, the startDate must be after 05/04/2009, and no revaluation
     is applied if the start date is before 01/01/1986.
     */
    val startDateObj = LocalDate.parse(
        startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )
    val post2009Date = LocalDate.parse(
        "06/04/2009",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )
    val noRevalDate = LocalDate.parse(
        "01/01/1986",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

    /*
     If the cap is 2.5%, the startDate must be after 05/04/2009, and no revaluation
     is applied if the start date is before 01/01/1986.
     */
    return if (startDateObj.isBefore(noRevalDate)) {
        return "01/01/1900" // This is just an error string.
    } else if (startDateObj.isBefore(post2009Date) && cap == 2.5) {
        "06/04/2009"
    } else {
        startDate
    }
}

/**
 * This calculates and returns the overall revaluation rate between two dates, using the
 * 12-month CPI and RPI percentages held in the local database. The calculation can use either a
 * 5% yearly cap, or a 2.5% yearly cap.
 */
internal fun revaluationCalculation(startDate: String, endDate: String,
                                    cpiRevList: List<CpiPercentage>,
                                    rpiRevList: List<RpiPercentage>,
                                    cap: Double, isRpiOnly: Boolean) : Double {

    /*
     We want to iterate through the lists backwards, from the end date to the start date.
     This is to line up the two lists. Both lists end at the same date (or should do), however
     rpi rates go back further in time.
     */
    val rpiRateList = rpiRevList.reversed()
    val cpiRateList = cpiRevList.reversed()

    // Check the start date is okay.
    val newStartDate = checkRevalDates(startDate, cap)

    // Need the amount of years to loop through the revaluation rates.
    val years = yearsCalculation(newStartDate, endDate).toInt()
    val latestSeptYearRpi = rpiRateList[0].year.toInt()
    val latestSeptYearCpi = cpiRateList[0].year.toInt()
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endYear = endDateObj.year

    /*
    If we don't hold the September RPI or CPI for the year, we want to return.
    Note that the revaluation rate for a year, is the preceding September.
     */
    val index = (latestSeptYearRpi + 1) - endYear
    val indexCpi = (latestSeptYearCpi + 1) - endYear
    if (index < 0 || indexCpi < 0) return -1.0 // We don't have the CPI or RPI values we need.
    if (newStartDate.equals("01/01/1900")) return 1.0 // This means no revaluation.

    var accumulatedRate = 1.0
    // Get the maximum revaluation rate allowed.
    val maxRevRate = (1 + (cap / 100)).pow(years)

    for (currentIndex in index until (index + years)) {
        // Get the lower of the rate of the cap (2.5 or 5.0).
        var rate: Double
        // CPI is used after 2010, before that RPI is used, using RPI list to prevent out of bounds.
        if (rpiRateList[currentIndex].year.toInt() > 2009 && !isRpiOnly) {
            rate = cpiRateList[currentIndex].value.replace("%","").toDouble()
        } else {
            rate = when (rpiRateList[currentIndex].year) {
                // These were errors with legislation in these years, change manually.
                "1986" -> 3.1
                "1988" -> 5.7
                else -> rpiRateList[currentIndex].value.replace("%", "").toDouble()
            }
        }

        // Turn this into something we can multiply by.
        rate = 1 + (rate / 100)

        accumulatedRate *= rate
    }

    // Need to get the figure to 3 decimal places.
    val formattedResult = BigDecimal(max(min(accumulatedRate, maxRevRate), 0.0))
        .setScale(3, RoundingMode.HALF_UP)

    // Return the result or 1.0, whichever is higher.
    return max(formattedResult.toDouble(), 1.0)

}