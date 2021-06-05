package com.example.android.plutus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CpiInflationViewModel : ViewModel() {

    // Currently unused.

}

@Suppress("UNCHECKED_CAST")
class CpiInflationViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CpiInflationViewModel() as T)
}