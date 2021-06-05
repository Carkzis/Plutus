package com.example.android.plutus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InflationMainViewModel : ViewModel() {

    // Currently unused.

}

@Suppress("UNCHECKED_CAST")
class InflationMainViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (InflationMainViewModel() as T)
}