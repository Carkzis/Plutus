package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.time.format.DateTimeFormatter

class DateCalcViewModel : ViewModel() {

    private val _startDateInfo = MutableLiveData("")
    val startDateInfo: LiveData<String>
        get() = _startDateInfo

    private val _endDateInfo = MutableLiveData("")
    val endDateInfo: LiveData<String>
        get() = _endDateInfo

    private val _dateCalcResults = MutableLiveData<DateCalcResults>()
    val dateCalcResults: LiveData<DateCalcResults>
        get() = _dateCalcResults

    private lateinit var results: DateCalcResults

    fun setStartDate(year: Int, month: Int, day: Int) {
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        _startDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    fun setEndDate(year: Int, month: Int, day: Int) {
        val formattedMonth = if (month < 10) "0$month" else month
        val formattedDay = if (day < 10) "0$day" else day
        _endDateInfo.value = "$formattedDay/$formattedMonth/$year"
    }

    fun calculateDateDifferences() {
        // TODO: Change this to actually calculate the results, this is just for testing purposes.
        results = DateCalcResults("10 years", "120 months", "521 weeks",
            "3650 days", "10 years and 0 months", "10 years and 0 days",
            "10 tax years", "10 6th Aprils passed")
        _dateCalcResults.value = results
    }

    fun addDefaultResultsVM(defaults: DateCalcResults) {
        // Will only assign the defaults if the value is null.
        _dateCalcResults.value = _dateCalcResults.value ?: defaults
    }

}

@Suppress("UNCHECKED_CAST")
class DateCalcViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (DateCalcViewModel() as T)
}