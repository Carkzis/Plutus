package com.example.android.plutus.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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

    return Benefits(
        formatAsCurrency(pcls), formatAsCurrency(residual), lta, formatAsCurrency(
        BigDecimal.ZERO)
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

    (0 until numberOfDays).forEach { day ->
        if (currentDateObj.monthValue == 4 && currentDateObj.dayOfMonth == 6) {
            sixthAprils += 1
        }
        currentDateObj = currentDateObj.plusDays(1)
    }

    return sixthAprils.toLong()
}
