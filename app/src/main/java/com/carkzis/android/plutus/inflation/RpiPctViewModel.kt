package com.carkzis.android.plutus.inflation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.data.Repository
import com.carkzis.android.plutus.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * ViewModel for the RpiPctFragment.
 */
@HiltViewModel
class RpiPctViewModel @Inject constructor(
    private val repository: Repository
    ) : ViewModel() {

    var inflationRates = repository.getRpiPercentages()

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    init {
        refreshRpiInflationRates()
    }

    /**
     * This is used for testing the refresh functionality.
     */
    fun testRefresh() {
        refreshRpiInflationRates()
    }

    /**
     * Refreshes the items in the database via the repository.
     */
    private fun refreshRpiInflationRates() {
        viewModelScope.launch {
            _loadingStatus.value = ApiLoadingStatus.LOADING
            try {
                repository.refreshRpiPercentages()
                _loadingStatus.value = ApiLoadingStatus.DONE
            } catch (e: Exception) {
                // Delay to show loading attempted via further visible spin of progress bar).
                delay(500)
                if (inflationRates.value.isNullOrEmpty()) {
                    _loadingStatus.value = ApiLoadingStatus.ERROR
                } else {
                    _loadingStatus.value = ApiLoadingStatus.DONE
                }
                R.string.connection_error.showToastMessage()
            }
        }
    }

    /**
     * Shows a toast message using a string id.
     */
    private fun Int.showToastMessage() {
        _toastText.value = Event(this)
    }

}