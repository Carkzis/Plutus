package com.example.android.plutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.math.RoundingMode
import java.text.DecimalFormat

private const val STANDARD_LTA = 1073100

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

    data class Benefits(
        val pcls: Double,
        val residualPension: Double,
        val lta: String,
        val dcFund: Double)

    private lateinit var dbBenefits : Benefits
    private lateinit var cmbBenefits : Benefits

    fun calculate() {

        // Return if any of these values have not been entered.
        fullPension.value ?: return showToastMessage("You need to enter a pension!")
        commutationFactor.value ?: return showToastMessage("You need to enter a commutation factor!")

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

        dbBenefits = dbPclsCalculation(fp, cf, dc)

        // Only call the combined pcls function if there is any money purchase fund value.
        if (dc > 0.0) {
            cmbBenefits = cmbPclsCalculation(fp, cf, dc)
        }

        // TODO: Update the results, remember to ensure that relevant fields are visible.

    }

    private var pcls: Double = 0.0
    private var commutedPension: Double = 0.0
    private var residual: Double = 0.0

    private var pclsLta: Double = 0.0
    private var residualLta: Double = 0.0
    private var dcFundLta: Double = 0.0

    private lateinit var lta: String
    private lateinit var decimalFormat: DecimalFormat

    fun dbPclsCalculation(
        fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {
        // Calculate the pcls and the residual pension
        pcls = (fullPension * commutationFactor * 20) / ((commutationFactor * 3) + 20)
        commutedPension = pcls / commutationFactor
        residual = fullPension - commutedPension
        lta = ltaCalculation(pcls, residual, dcFund)

        return Benefits(pcls, residual, lta, dcFund)
    }

    fun cmbPclsCalculation(
        fullPension: Double, commutationFactor: Double, dcFund: Double = 0.0): Benefits {
        // Calculate the pcls and residual when combined with the dc fund
        pcls = dcFund + (commutationFactor * ((fullPension * 20) - (dcFund * 3)) /
                ((commutationFactor * 3) + 20))
        commutedPension = (pcls - dcFund) / commutationFactor
        residual = fullPension - commutedPension
        lta = ltaCalculation(pcls, residual, dcFund)

        return Benefits(pcls, residual, lta, dcFund)
    }

    private fun ltaCalculation(pcls: Double, residual: Double, dcFund: Double) : String {

        // Variable to format lifetime allowance to 2 decimal places (rounded down).
        decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.DOWN

        // Calculate the individual LTAs, and convert them back into doubles after rounding.
        pclsLta =
            decimalFormat.format((pcls / STANDARD_LTA) * 100).toDouble()
        residualLta =
            decimalFormat.format((residual * 20) / STANDARD_LTA * 100).toDouble()
        dcFundLta =
            decimalFormat.format((dcFund / STANDARD_LTA) * 100).toDouble()

        // Then add them together to give the total LTA (and convert back to a string).
        return "${(pclsLta + residualLta + dcFundLta)}%"

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