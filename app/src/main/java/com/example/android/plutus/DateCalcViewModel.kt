package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DateCalcViewModel : ViewModel() {

    private val _startDateInfo = MutableLiveData("")
    val startDateInfo: LiveData<String>
        get() = _startDateInfo

    private val _endDateInfo = MutableLiveData("")
    val endDateInfo: LiveData<String>
        get() = _endDateInfo

    fun setStartDate(year: Int, month: Int, day: Int) {
        _startDateInfo.value = "$day/$month/$year"
    }

    fun setEndDate(year: Int, month: Int, day: Int) {
        _endDateInfo.value = "$day/$month/$year"
    }
}

@Suppress("UNCHECKED_CAST")
class DateCalcViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (DateCalcViewModel() as T)
}