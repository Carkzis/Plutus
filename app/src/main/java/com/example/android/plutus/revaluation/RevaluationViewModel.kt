package com.example.android.plutus.revaluation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.data.Repository
import com.example.android.plutus.inflation.ApiLoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RevaluationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // TODO: Check these are what we expect.
    var cpiPercentages = repository.getCpiPercentages()
    var rpiPercentages = repository.getRpiPercentages()

    var startDateInfo = MutableLiveData("")
    var endDateInfo = MutableLiveData("")

    private var _loadingStatus = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _loadingStatus

    init {
        showIt()
    }

    fun showIt() {
        Timber.e(cpiPercentages.value.toString())
    }
}