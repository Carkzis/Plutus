package com.example.android.plutus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PclsViewModel : ViewModel() {

}

@Suppress("UNCHECKED_CAST")
class PclsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PclsViewModel() as T)
}