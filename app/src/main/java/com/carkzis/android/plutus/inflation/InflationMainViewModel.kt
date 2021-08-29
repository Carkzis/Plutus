package com.carkzis.android.plutus.inflation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InflationMainViewModel @Inject constructor() : ViewModel() {

    // Currently unused.

}

enum class ApiLoadingStatus { LOADING, ERROR, DONE }