package com.carkzis.android.plutus.revaluation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.RevalResults
import com.carkzis.android.plutus.data.Repository
import com.carkzis.android.plutus.inflation.ApiLoadingStatus
import com.carkzis.android.plutus.*
import com.carkzis.android.plutus.util.*
import com.carkzis.android.plutus.util.daysCalculation
import com.carkzis.android.plutus.util.gmpRevaluationCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.Year
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class RevaluationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var cpiPercentages = repository.getSeptemberCpi()
    var rpiPercentages = repository.getSeptemberRpi()

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

    /**
     * This holds the calculation results.
     */
    private var _revalCalcResults = MutableLiveData<RevalResults>()
    val revalCalcResults: LiveData<RevalResults>
        get() = _revalCalcResults

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    /**
     This LiveData is used for testing the different Toast messages only.
     */
    private var _toastTest = MutableLiveData<String>()
    val toastTest: LiveData<String>
        get() = _toastTest

    /**
     * Initialise the loading status to DONE as default.
     */
    init {
        _loadingStatus.value = ApiLoadingStatus.DONE
    }

    private lateinit var results: RevalResults

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

        // If the difference is negative, return a toast message
        if (periodDays < 1) return showToastMessage(R.string.end_date_after_start_date)

        val cpiPcts = cpiPercentages.value
        val rpiPcts = rpiPercentages.value

        // Check if there is no data held, or if it is otherwise out of date
        if (cpiPercentages.value.isNullOrEmpty() || rpiPercentages.value.isNullOrEmpty()) {
            // Stop attempting a calculation, but attempt refresh of cache
            refreshCpiAndRpiCache()
            _toastTest.value = "Update Inflation First"
            return showToastMessage(R.string.update_inflation_first)
        } else if (cpiPcts!!.last().year.toInt() != rpiPcts!!.last().year.toInt()) {
            refreshCpiAndRpiCache()
            _toastTest.value = "CPI and RPI Mismatch"
            return showToastMessage(R.string.reval_error)
        } else if ((cpiPcts!!.last().year.toInt() < Year.now().value - 1) ||
            (rpiPcts!!.last().year.toInt() < Year.now().value - 1)) {
            showToastMessage(R.string.update_inflation)
            _toastTest.value = "Data Needs Updating"
            refreshCpiAndRpiCache()
        }
        calculateRevaluationRates()
    }

    /**
     * This is used for testing the refresh functionality.
     */
    fun testRefresh() {
        refreshCpiAndRpiCache()
    }

    /**
     * This will refresh both the CPI and RPI cache. It will await all results, and inform
     * the user if the network call was successful. We need these result in order to calculate the
     * benefits.
     */
    private fun refreshCpiAndRpiCache() {
        viewModelScope.launch {
            _loadingStatus.value = ApiLoadingStatus.LOADING
            var isSuccess = false
                awaitAll(async {
                    try {
                        repository.refreshCpiPercentages()
                        isSuccess = true
                    } catch (e: Exception) {
                        _loadingStatus.value = ApiLoadingStatus.ERROR
                        showToastMessage(R.string.connection_error)
                    }
                }, async {
                    try {
                        repository.refreshRpiPercentages()
                        isSuccess = true
                    } catch (e: Exception) {
                        _loadingStatus.value = ApiLoadingStatus.ERROR
                        showToastMessage(R.string.connection_error)
                    }
                })
            if (isSuccess) {
                _loadingStatus.value = ApiLoadingStatus.DONE
                showToastMessage(R.string.inflation_data_refreshed)
            }
        }
    }

    /**
     * This will calculate all the revaluation rates, and add the results to the LiveData
     * to display to the user via the UI.
     */
    private fun calculateRevaluationRates() {
        results = RevalResults(
            max(
                revaluationCalculation(
                    startDateInfo.value!!, endDateInfo.value!!,
                    cpiPercentages.value!!, rpiPercentages.value!!, 5.0, false
                ), 0.0
            ),
            max(
                revaluationCalculation(
                    startDateInfo.value!!, endDateInfo.value!!,
                    cpiPercentages.value!!, rpiPercentages.value!!, 2.5, false
                ), 0.0
            ),
            max(
                revaluationCalculation(
                    startDateInfo.value!!, endDateInfo.value!!,
                    cpiPercentages.value!!, rpiPercentages.value!!, 5.0, true
                ), 0.0
            ),
            max(
                revaluationCalculation(
                    startDateInfo.value!!, endDateInfo.value!!,
                    cpiPercentages.value!!, rpiPercentages.value!!, 2.5, true
                ), 0.0
            ),
            gmpRevaluationCalculation(startDateInfo.value!!, endDateInfo.value!!, true),
            gmpRevaluationCalculation(startDateInfo.value!!, endDateInfo.value!!, false),
        )

        // If they are both one, inform member there is no revaluation.
        if (results.cpiHigh == 1.0 && results.rpiHigh == 1.0) {
            showToastMessage(R.string.no_revaluation)
            _toastTest.value = "No CPI or RPI"
        // If they are both zero, inform the member we do not have the available revaluation rates.
        } else if (results.cpiHigh == 0.0 && results.rpiHigh == 0.0) {
            showToastMessage(R.string.no_future_reval)
            _toastTest.value = "Too Far Ahead"
        // If cpiHigh is 0.0 but rpiHigh isn't, something has gone wrong, as the CPI percentages
        // will have been updated but the RPI percentages aren't. Set the RPI results to 0.0 so we
        // are not giving any false information, and inform the user.
        } else if (results.cpiHigh == 0.0 && results.rpiHigh != 0.0) {
            results.rpiHigh = 0.0
            results.rpiLow = 0.0
            showToastMessage(R.string.reval_error)
            _toastTest.value = "Something Has Gone Wrong"
        // Same in the other direction.
        } else if (results.cpiHigh != 0.0 && results.rpiHigh == 0.0) {
            results.cpiHigh = 0.0
            results.cpiLow = 0.0
            showToastMessage(R.string.reval_error)
            _toastTest.value = "Something Has Gone Wrong"
        }

        _revalCalcResults.value = results
    }

    /**
     * Add the toast string id, in an Event class, to the LiveData.
     */
    private fun showToastMessage(message: Int) {
        _toastText.value = Event(message)
    }

}