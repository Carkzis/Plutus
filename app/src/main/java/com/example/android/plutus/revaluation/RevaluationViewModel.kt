package com.example.android.plutus.revaluation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.plutus.*
import com.example.android.plutus.data.Repository
import com.example.android.plutus.inflation.ApiLoadingStatus
import com.example.android.plutus.util.daysCalculation
import com.example.android.plutus.util.gmpRevaluationCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RevaluationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // TODO: Check that we have the most current data, else refresh.
    var cpiPercentages = repository.getSeptemberCpi()
    var rpiPercentages = repository.getSeptemberRpi()

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

    private var _revalCalcResults = MutableLiveData<RevalResults>()
    val revalCalcResults: LiveData<RevalResults>
        get() = _revalCalcResults

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    private lateinit var results: RevalResults

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

        calculateRevaluationRates()
    }

    fun calculateRevaluationRates() {
        results = RevalResults(
            1.0,
            1.0,
            1.0,
            1.0,
            gmpRevaluationCalculation(startDateInfo.value!!, endDateInfo.value!!, true),
            gmpRevaluationCalculation(startDateInfo.value!!, endDateInfo.value!!, false),
        )
        Timber.e(cpiPercentages.value.toString())
        _revalCalcResults.value = results
    }

    private fun showToastMessage(message: Int) {
        _toastText.value = Event(message)
    }


}