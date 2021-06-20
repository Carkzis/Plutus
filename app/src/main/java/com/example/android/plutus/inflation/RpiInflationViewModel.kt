package com.example.android.plutus.inflation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.plutus.Event
import com.example.android.plutus.R
import com.example.android.plutus.data.Repository
import com.example.android.plutus.inflation.ApiLoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RpiInflationViewModel @Inject constructor(
    private val repository: Repository
    ) : ViewModel() {

    var inflationRates = repository.getRpiRates()

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    init {
        refreshRpiInflationRates()
    }

    private fun refreshRpiInflationRates() {
        viewModelScope.launch {
            _loadingStatus.value = ApiLoadingStatus.LOADING
            try {
                repository.refreshRpiInflation()
                _loadingStatus.value = ApiLoadingStatus.DONE
            } catch (e: Exception) {
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