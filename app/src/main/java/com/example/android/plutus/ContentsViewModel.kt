package com.example.android.plutus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContentsViewModel : ViewModel() {


}

@Suppress("UNCHECKED_CAST")
class ContentsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ContentsViewModel() as T)
}
