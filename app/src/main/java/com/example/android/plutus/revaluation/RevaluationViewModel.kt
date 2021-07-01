package com.example.android.plutus.revaluation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.plutus.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RevaluationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

}