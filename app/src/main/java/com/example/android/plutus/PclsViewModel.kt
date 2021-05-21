package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber

class PclsViewModel : ViewModel() {

    // LiveData variable inputs.
    var fullPension = MutableLiveData<String>()
    var commutationFactor = MutableLiveData<String>()
    var dcFund = MutableLiveData<String>()

    // LiveData variable outputs.
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

    // LiveData feedback e.g. Toast
    private var _toastText = MutableLiveData<String>()
    val toastText: LiveData<String>
        get() = _toastText

    private lateinit var dbBenefits : Benefits
    private lateinit var cmbBenefits : Benefits

    fun validateBeforeCalculation() {
        // Return if any of these values have not been entered.
        fullPension.value ?: return showToastMessage("You need to enter a pension!")
        commutationFactor.value ?: return showToastMessage(
            "You need to enter a commutation factor!")

        // Show a toast if any of the values have been cleared, giving empty strings (not null).
        if (fullPension.value.equals("") || fullPension.value.equals("."))
            return showToastMessage("You need to enter a pension!")
        if (commutationFactor.value.equals(""))
            return showToastMessage("You need to enter a commutation factor!")

        // Convert String values to Doubles.
        val fp = fullPension.value?.toDouble()!!
        val cf = commutationFactor.value?.toDouble()!!
        // If the fund value is null or empty, this becomes 0.0
        val dc = if (dcFund.value.equals("")) 0.0 else dcFund.value?.toDouble() ?: 0.0

        if (fp <= 0 || cf <= 0) {
            return showToastMessage("The pension and commutation factors can't be 0!")
        }

        calculationWrapper(fp, cf, dc)
    }

    private fun calculationWrapper(fp: Double, cf: Double, dc: Double) {
        // Call function to calculate db benefits, and return a data object with the results
        dbBenefits = dbPclsCalculation(fp, cf, dc)
        // Only call the combined pcls function if there is any money purchase fund value.
        if (dc > 0.0) {
            cmbBenefits = cmbPclsCalculation(fp, cf, dc)
        }
        updateWithResults()
    }

    private fun updateWithResults() {
        Timber.w("Checking that update with results works!")
    }

    private fun showToastMessage(message: String) {
        _toastText.value = message
    }

}

@Suppress("UNCHECKED_CAST")
class PclsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PclsViewModel() as T)
}