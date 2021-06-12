package com.example.android.plutus

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

enum class CpiApiLoadingStatus { LOADING, ERROR, DONE }

@HiltViewModel
class CpiInflationViewModel @Inject constructor(
    private val repository: InflationRepository) : ViewModel() {

    val inflationRates = repository.cpiInflationRates

    private var _loadingStatus = MutableLiveData<CpiApiLoadingStatus>()
    val loadingStatus: LiveData<CpiApiLoadingStatus>
        get() = _loadingStatus

//    private var _inflationRates = MutableLiveData<List<InflationRate>>()
//    val inflationRates: LiveData<List<InflationRate>>
//        get() = _inflationRates

    init {
        refreshCpiInflationRates()
    }

    private fun refreshCpiInflationRates() {
        viewModelScope.launch {
            _loadingStatus.value = CpiApiLoadingStatus.LOADING
            try {
                repository.refreshInflation()
                _loadingStatus.value = CpiApiLoadingStatus.DONE
                Timber.e("It worked!")
            } catch (e: Exception) {
                if (inflationRates.value.isNullOrEmpty()) {
                    _loadingStatus.value = CpiApiLoadingStatus.ERROR
                } else {
                    _loadingStatus.value = CpiApiLoadingStatus.DONE
                }
                Timber.e("Failure!")
            }
        }
    }

}