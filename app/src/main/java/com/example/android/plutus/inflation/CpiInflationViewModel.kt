package com.example.android.plutus

import androidx.lifecycle.*
import com.example.android.plutus.data.InflationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CpiInflationViewModel @Inject constructor(
    private val repository: InflationRepository
) : ViewModel() {

    val inflationRates = repository.cpiInflationRates

    private var _loadingStatus = MutableLiveData<CpiApiLoadingStatus>()
    val loadingStatus: LiveData<CpiApiLoadingStatus>
        get() = _loadingStatus

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    init {
        refreshCpiInflationRates()
    }

    private fun refreshCpiInflationRates() {
        viewModelScope.launch {
            _loadingStatus.value = CpiApiLoadingStatus.LOADING
            try {
                repository.refreshInflation()
                _loadingStatus.value = CpiApiLoadingStatus.DONE
            } catch (e: Exception) {
                if (inflationRates.value.isNullOrEmpty()) {
                    _loadingStatus.value = CpiApiLoadingStatus.ERROR
                } else {
                    _loadingStatus.value = CpiApiLoadingStatus.DONE
                }
                R.string.connection_error.showToastMessage()
            }
        }
    }

    private fun Int.showToastMessage() {
        _toastText.value = Event(this)
    }

}

enum class CpiApiLoadingStatus { LOADING, ERROR, DONE }