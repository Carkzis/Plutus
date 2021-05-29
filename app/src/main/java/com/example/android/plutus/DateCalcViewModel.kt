package com.example.android.plutus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DateCalcViewModel : ViewModel() {

}

@Suppress("UNCHECKED_CAST")
class DateCalcViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (DateCalcViewModel() as T)
}