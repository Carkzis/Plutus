package com.carkzis.android.plutus.dates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carkzis.android.plutus.DateCalcResults
import com.carkzis.android.plutus.Event
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.util.daysCalculation
import com.carkzis.android.plutus.util.monthsCalculation
import com.carkzis.android.plutus.util.sixthAprilsPassCalculation
import com.carkzis.android.plutus.util.taxYearsCalculation
import com.carkzis.android.plutus.util.weeksCalculation
import com.carkzis.android.plutus.util.yearsAndDaysCalculation
import com.carkzis.android.plutus.util.yearsAndMonthsCalculation
import com.carkzis.android.plutus.util.yearsCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the DateCalcFragment.
 */
@HiltViewModel
class DateCalcViewModel @Inject constructor() : ViewModel() {

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

    /**
     * This holds the calculation results.
     */
    private var _dateCalcResults = MutableLiveData<DateCalcResults>()
    val dateCalcResults: LiveData<DateCalcResults>
        get() = _dateCalcResults

    /**
     * This LiveData holds a toast event, taking in an Int which will be a String id.
     */
    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    private lateinit var results: DateCalcResults

    /**
     * This sets the start date edittext with the data provided in the correct format.
     */
    fun setStartDate(year: Int, month: Int, day: Int) {
        // Ensure single digits have a preceding 0.
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        startDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    /**
     * This sets the end date edittext with the data provided in the correct format.
     */
    fun setEndDate(year: Int, month: Int, day: Int) {
        // Ensure single digits have a preceding 0.
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        endDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    /**
     * Validates the entered data, and returns an error message if there is something missing
     * or incorrect.
     */
    fun validateBeforeCalculation() {
        // Return if these values have not been entered.
        if (startDateInfo.value == "") return showToastMessage(R.string.no_start_date_entered)
        if (endDateInfo.value == "") return showToastMessage(R.string.no_end_date_entered)

        val periodDays = daysCalculation(startDateInfo.value!!, endDateInfo.value!!)

        // If the difference is negative, return a toast message.
        if (periodDays < 1) return showToastMessage(R.string.end_date_after_start_date)

        calculateDateDifferences()
    }

    /**
     * Returns a result data class, using the various calculations held in CalcUtils.kt.
     */
    private fun calculateDateDifferences() {
        results = DateCalcResults(
            yearsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            monthsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            weeksCalculation(startDateInfo.value!!, endDateInfo.value!!),
            daysCalculation(startDateInfo.value!!, endDateInfo.value!!),
            yearsAndMonthsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            yearsAndDaysCalculation(startDateInfo.value!!, endDateInfo.value!!),
            taxYearsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            sixthAprilsPassCalculation(startDateInfo.value!!, endDateInfo.value!!)
        )
        _dateCalcResults.value = results
    }

    /**
     * Assigns the defaults (0 across the board) if there is no calculation completed.
     */
    fun addDefaultResultsVM(defaults: DateCalcResults) {
        // Will only assign the defaults if the value is null.
        _dateCalcResults.value = _dateCalcResults.value ?: defaults
    }

    /**
     * Add the toast string id, in an Event class, to the LiveData.
     */
    private fun showToastMessage(message: Int) {
        _toastText.value = Event(message)
    }

}