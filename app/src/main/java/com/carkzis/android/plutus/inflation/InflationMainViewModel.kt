package com.carkzis.android.plutus.inflation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Fragment for displaying the inflation contents.
 */
@HiltViewModel
class InflationMainViewModel @Inject constructor() : ViewModel() {
    // Currently unused.
}

/**
 * This displays the loading status after a network call, where LOADING is where a response
 * is being awaiting, ERROR means there was an error with the network call, and DONE
 * means a response was received.
 */
enum class ApiLoadingStatus { LOADING, ERROR, DONE }