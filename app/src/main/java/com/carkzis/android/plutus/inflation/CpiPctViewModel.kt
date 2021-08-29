package com.carkzis.android.plutus

import androidx.lifecycle.*
import com.carkzis.android.plutus.data.Repository
import com.carkzis.android.plutus.inflation.ApiLoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CpiPctViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var inflationRates = repository.getCpiPercentages()

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    init {
        refreshCpiInflationRates()
    }

    // TODO: Remove when no further testing required.
    fun testRefresh() {
        refreshCpiInflationRates()
    }

    private fun refreshCpiInflationRates() {
        viewModelScope.launch {
            _loadingStatus.value = ApiLoadingStatus.LOADING
            try {
                repository.refreshCpiPercentages()
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

    private fun Int.showToastMessage() {
        _toastText.value = Event(this)
    }

}

