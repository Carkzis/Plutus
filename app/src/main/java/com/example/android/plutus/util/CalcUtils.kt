package com.example.android.plutus.util

import com.example.android.plutus.Benefits
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage
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

internal fun smallCmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {

    // Calculate the pcls and residual when combined with the dc fund
    val pcls = BigDecimal(dcFund + ((commutationFactor * ((fullPension * 20) - (dcFund * 3))) /
            ((commutationFactor * 3) + 20))).setScale(2, RoundingMode.HALF_EVEN)
    val commutedPension = BigDecimal((pcls.toDouble() - dcFund) / commutationFactor)
        .setScale(2, RoundingMode.HALF_EVEN)
    val residual = BigDecimal(fullPension - commutedPension.toDouble())
        .setScale(2, RoundingMode.HALF_EVEN)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), 0.0)

    return Benefits(
        formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(
        BigDecimal.ZERO)
    )
}

internal fun largeCmbPclsCalculation(
    fullPension: Double, commutationFactor: Double, residualDcForAnnuity: Boolean,
    dcFund: Double = 0.0): Benefits {

    // This is the largest amount of DC that can be combined with the DB benefits to make a PCLS.
    val maxDcToCombine = BigDecimal((fullPension * 20) / 3)
        .setScale(2, RoundingMode.HALF_EVEN)

    // PCLS is calculated differently depending on if DC is used to buy an annuity
    //  or taken as a UFPLS
    val pcls = if (residualDcForAnnuity) BigDecimal(((fullPension * 20) + dcFund) / 4)
        .setScale(2, RoundingMode.HALF_EVEN) else maxDcToCombine
    // Residual will be the same as the full pension with a large lump sum.
    val residual = BigDecimal(fullPension)
        .setScale(2, RoundingMode.HALF_EVEN)
    val dc = BigDecimal(dcFund - pcls.toDouble())
        .setScale(2, RoundingMode.HALF_EVEN)
    val lta = ltaCalculation(pcls.toDouble(), residual.toDouble(), dc.toDouble())

    return Benefits(
        formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(dc)
    )
}

internal fun ltaCalculation(pcls: Double, pension: Double, dcFund: Double = 0.0) : String {

    // Calculate the individual LTAs, rounding them (down) to 2 dp
    val pclsLta =
        BigDecimal((pcls / STANDARD_LTA) * 100).setScale(2, RoundingMode.DOWN)
    val pensionLta =
        BigDecimal((pension * 20) / STANDARD_LTA * 100).setScale(2, RoundingMode.DOWN)
    val dcFundLta =
        BigDecimal((dcFund / STANDARD_LTA) * 100).setScale(2, RoundingMode.DOWN)

    // Then add them together to give the total LTA (and convert back to a string).
    return "${(pclsLta + pensionLta + dcFundLta)}%"

}

internal fun formatAsCurrency(value: BigDecimal) : String {
    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    return currencyFormat.format(value.toDouble())
}

internal fun daysCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    // Last day is included in days calculation.
    return ChronoUnit.DAYS.between(startDateObj, endDateObj)
}

internal fun yearsCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.YEARS.between(startDateObj, endDateObj)
}

internal fun monthsCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.MONTHS.between(startDateObj, endDateObj)
}

internal fun weeksCalculation(startDate: String, endDate: String) : Long {
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    return ChronoUnit.WEEKS.between(startDateObj, endDateObj)
}

internal fun yearsAndMonthsCalculation(startDate: String, endDate: String) : Pair<Long, Long> {
    val totalMonths = monthsCalculation(startDate, endDate)
    val years = totalMonths / 12
    val months = totalMonths % 12
    return Pair(years, months)
}

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

internal fun taxYearsCalculation(startDate: String, endDate: String) : Long {
    // Check if start date is before 06/04/XXXX, if so just change day and month to
    // change to 06/04/XXXX, otherwise change it to 06/04/XXXX+1
    // Do reverse for end date
    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
    val startDateYearsTaxYearStart = LocalDate.parse("06/04/${startDateObj.year}",
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endDateYearsTaxYearEnd = LocalDate.parse("05/04/${endDateObj.year}",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)

    // Choose which is the first tax year start date after the input start date
    val taxYearStartDate =
        if (ChronoUnit.DAYS.between(startDateObj, startDateYearsTaxYearStart) >= 0)  {
            startDateYearsTaxYearStart
        } else {
            LocalDate.parse("06/04/${startDateObj.year + 1}",
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

    // Choose which is the first tax year end date before the input end date
    val taxYearEndDate =
        if (ChronoUnit.DAYS.between(endDateObj, endDateYearsTaxYearEnd) <= 0) {
            endDateYearsTaxYearEnd
        } else {
            // Note: tax year ends 05/04 of each year, but add one day for whole year calc purposes
            LocalDate.parse("05/04/${endDateObj.year - 1}",
                DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)
        }

    // Cannot be less than 0, which will occur if the start date is after 6 April in one
    // year, and the end date is before 5 April in the following year
    return maxOf(ChronoUnit.YEARS.between(taxYearStartDate, taxYearEndDate), 0)
}

internal fun sixthAprilsPassCalculation(startDate: String, endDate: String) : Long {

    val numberOfDays = daysCalculation(startDate, endDate)

    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    var currentDateObj = startDateObj
    var sixthAprils = 0

    (0 until numberOfDays).forEach { _ ->
        if (currentDateObj.monthValue == 4 && currentDateObj.dayOfMonth == 6) {
            sixthAprils += 1
        }
        currentDateObj = currentDateObj.plusDays(1)
    }

    return sixthAprils.toLong()
}

internal fun gmpRevaluationCalculation(startDate: String, endDate: String,
                                       isFullTaxYears: Boolean) : Double {

    val gmpStartDate = LocalDate.parse(
        "06/04/1978",
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

    val startDateObj = LocalDate.parse(startDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val validatedStartDate: String

    // GMP started on 06/04/1978, so limited the start date to this
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
        .setScale(3, RoundingMode.HALF_EVEN)

    return formattedResult.toDouble()
}

internal fun checkRevalDates(startDate: String, cap: Double): String {

    // If the cap is 2.5%, the startDate must be after 05/04/2009, and no revaluation
    // is applied if the start date is before 01/01/1986
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

    // If the cap is 2.5%, the startDate must be after 05/04/2009, and no revaluation
    // is applied if the start date is before 01/01/1986
    return if (startDateObj.isBefore(noRevalDate)) {
        return "1.0"
    } else if (startDateObj.isBefore(post2009Date) && cap == 2.5) {
        "06/04/2009"
    } else {
        startDate
    }
}

internal fun rpiRevaluationCalculation(startDate: String, endDate: String,
                                       revList: List<RpiPercentage>, cap: Double) : Double {

    val rateList = revList.reversed()

    val newStartDate = checkRevalDates(startDate, cap)
    if (newStartDate.equals("1.0")) return 1.0

    // Need the amount of years to loop through the revaluation rates
    val years = yearsCalculation(newStartDate, endDate).toInt()
    val latestSeptYear = rateList[0].year.toInt()
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endYear = endDateObj.year

    // If we don't hold the September RPI for the year, we want to return
    val index = (latestSeptYear + 1) - endYear
    if (index < 0) return -1.0

    var accumulatedRate = 1.0
    // Get the maximum revaluation rate allowed.
    val maxRevRate = (1 + (cap / 100)).pow(years)

    for (currentIndex in index until (index + years)) {
        // get the lower of the rate of the cap (2.5 or 5.0)
        var rate = when (rateList[currentIndex].year) {
            "1986" -> 3.1 // these were errors with legislation in these years, so change manually
            "1987" -> 5.7
            else -> rateList[currentIndex].value.replace("%","").toDouble()
        }

        // Turn this into something we can multiply by
        rate = 1 + (rate / 100)

        accumulatedRate *= rate
    }

    // Need to get the figure to 3 decimal places.
    val formattedResult = BigDecimal(max(min(accumulatedRate, maxRevRate), 0.0))
        .setScale(3, RoundingMode.HALF_EVEN)

    return formattedResult.toDouble()

}

internal fun cpiRevaluationCalculation(startDate: String, endDate: String,
                                       cpiRevList: List<CpiPercentage>,
                                       rpiRevList: List<RpiPercentage>, cap: Double) : Double {

    val rpiRateList = rpiRevList.reversed()
    val cpiRateList = cpiRevList.reversed()

    val newStartDate = checkRevalDates(startDate, cap)
    if (newStartDate.equals("1.0")) return 1.0

    // Need the amount of years to loop through the revaluation rates
    val years = yearsCalculation(newStartDate, endDate).toInt()
    val latestSeptYear = rpiRateList[0].year.toInt()
    val endDateObj = LocalDate.parse(endDate,
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val endYear = endDateObj.year

    // If we don't hold the September RPI for the year, we want to return
    val index = (latestSeptYear + 1) - endYear
    if (index < 0) return -1.0

    var accumulatedRate = 1.0
    // Get the maximum revaluation rate allowed.
    val maxRevRate = (1 + (cap / 100)).pow(years)

    for (currentIndex in index until (index + years)) {
        // get the lower of the rate of the cap (2.5 or 5.0)
        var rate: Double
        // CPI is used after 2010, before that RPI is used, using RPI list to prevent out of bounds
        if (rpiRateList[currentIndex].year.toInt() > 2009) {
            rate = cpiRateList[currentIndex].value.replace("%","").toDouble()
        } else {
            rate = when (rpiRateList[currentIndex].year) {
                "1986" -> 3.1 // these were errors with legislation in these years, change manually
                "1988" -> 5.7
                else -> rpiRateList[currentIndex].value.replace("%", "").toDouble()
            }
        }

        // Turn this into something we can multiply by
        rate = 1 + (rate / 100)

        accumulatedRate *= rate
    }

    // Need to get the figure to 3 decimal places.
    val formattedResult = BigDecimal(max(min(accumulatedRate, maxRevRate), 0.0))
        .setScale(3, RoundingMode.HALF_EVEN)

    return formattedResult.toDouble()

}