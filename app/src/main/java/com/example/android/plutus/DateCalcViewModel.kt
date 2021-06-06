package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateCalcViewModel @Inject constructor() : ViewModel() {

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

    private val _dateCalcResults = MutableLiveData<DateCalcResults>()
    val dateCalcResults: LiveData<DateCalcResults>
        get() = _dateCalcResults

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    private lateinit var results: DateCalcResults

    fun setStartDate(year: Int, month: Int, day: Int) {
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        startDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    fun setEndDate(year: Int, month: Int, day: Int) {
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        endDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    fun validateBeforeCalculation() {
        // Return if these values have not been entered.
        if (startDateInfo.value == "") return showToastMessage(R.string.no_start_date_entered)
        if (endDateInfo.value == "") return showToastMessage(R.string.no_end_date_entered)

        val periodDays = daysCalculation(startDateInfo.value!!, endDateInfo.value!!)

        // If the difference is negative, return a toast message
        if (periodDays < 1) return showToastMessage(R.string.end_date_after_start_date)

        calculateDateDifferences()
    }

    private fun calculateDateDifferences() {

        results = DateCalcResults(
            yearsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            monthsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            weeksCalculation(startDateInfo.value!!, endDateInfo.value!!),
            daysCalculation(startDateInfo.value!!, endDateInfo.value!!),
            yearsAndMonthsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            yearsAndDaysCalculation(startDateInfo.value!!, endDateInfo.value!!),
            taxYearsCalculation(startDateInfo.value!!, endDateInfo.value!!),
            sixthAprilsPassCalculation(startDateInfo.value!!, endDateInfo.value!!))
        _dateCalcResults.value = results
    }

    fun addDefaultResultsVM(defaults: DateCalcResults) {
        // Will only assign the defaults if the value is null.
        _dateCalcResults.value = _dateCalcResults.value ?: defaults
    }

    private fun showToastMessage(message: Int) {
        _toastText.value = Event(message)
    }

}