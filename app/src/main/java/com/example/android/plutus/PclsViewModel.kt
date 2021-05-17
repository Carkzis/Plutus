package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PclsViewModel : ViewModel() {

    // Setting up LiveData variables.
    private var _dbPcls = MutableLiveData<Double>()
    val dbPcls: LiveData<Double>
        get() = _dbPcls

    private var _dbResidualPension = MutableLiveData<Double>()
    val dbResidual: LiveData<Double>
        get() = _dbResidualPension

    private var _dbMoneyPurchaseFund = MutableLiveData<Double>()
    val dbMoneyPurchaseFund: LiveData<Double>
        get() = _dbMoneyPurchaseFund

    private var _dbLifetimeAllowance = MutableLiveData<Double>()
    val dbLifetimeAllowance: LiveData<Double>
        get() = _dbLifetimeAllowance

    private var _combinedPcls = MutableLiveData<Double>()
    val combinedPcls: LiveData<Double>
        get() = _combinedPcls

    private var _combinedResidualPension = MutableLiveData<Double>()
    val combinedResidual: LiveData<Double>
        get() = _combinedResidualPension

    private var _combinedLifetimeAllowance = MutableLiveData<Double>()
    val combinedLifetimeAllowance: LiveData<Double>
        get() = _combinedLifetimeAllowance

}

@Suppress("UNCHECKED_CAST")
class PclsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PclsViewModel() as T)
}