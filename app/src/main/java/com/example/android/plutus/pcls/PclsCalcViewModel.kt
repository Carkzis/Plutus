package com.example.android.plutus.pcls

import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.plutus.*
import com.example.android.plutus.Benefits
import com.example.android.plutus.util.*
import com.example.android.plutus.util.dbPclsCalculation
import com.example.android.plutus.util.formatAsCurrency
import com.example.android.plutus.util.largeCmbPclsCalculation
import com.example.android.plutus.util.ltaCalculation
import com.example.android.plutus.util.smallCmbPclsCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PclsCalcViewModel @Inject constructor() : ViewModel() {

    // LiveData variable inputs.
    var fullPension = MutableLiveData<String>()
    var commutationFactor = MutableLiveData<String>()
    var dcFund = MutableLiveData<String>()

    var spinnerPosition = MutableLiveData<Int>()
    var sLtaList = arrayListOf("1,073,100.00",
    "1,055,000.00",
    "1,030,000.00",
    "1,000,000.00",
    "1,250,000.00",
    "1,500,000.00",
    "1,800,000.00",
    "1,750,000.00",
    "1,650,000.00",
    "1,600,000.00")
    var _standardLta = MutableLiveData<String>()
    val standardLta: LiveData<String>
        get() = _standardLta

    fun setStandardLta(position: Int) {
        _standardLta.value = sLtaList[position]
    }

    // LiveData variable outputs.
    private val _dbBenOutput = MutableLiveData<Benefits>()
    val dbBenOutput: LiveData<Benefits>
        get() = _dbBenOutput

    private val _cmbBenOutput1 = MutableLiveData<Benefits>()
    val cmbBenOutput1: LiveData<Benefits>
        get() = _cmbBenOutput1

    private val _cmbBenOutput2 = MutableLiveData<Benefits>()
    val cmbBenOutput2: LiveData<Benefits>
        get() = _cmbBenOutput2

    private val _noPclsBenOutput = MutableLiveData<Benefits>()
    val noPclsBenOutput: LiveData<Benefits>
        get() = _noPclsBenOutput

    // LiveData feedback e.g. Toast
    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>>
        get() = _toastText

    private lateinit var dbBenefits : Benefits
    private lateinit var noPclsBenefits : Benefits
    private var cmbBenefits1 : Benefits? = null
    private var cmbBenefits2 : Benefits? = null

    fun validateBeforeCalculation() {
        // Return if any of these values have not been entered.
        fullPension.value ?: return showToastMessage(R.string.no_pension_toast)
        commutationFactor.value ?: return showToastMessage(R.string.no_cf_toast)

        // Show a toast if any of the values have been cleared, giving empty strings (not null).
        if (fullPension.value.equals("") || fullPension.value.equals("."))
            return showToastMessage(R.string.no_pension_toast)
        if (commutationFactor.value.equals("") || commutationFactor.value.equals("."))
            return showToastMessage(R.string.no_cf_toast)

        // Convert String values to Doubles.
        val fp = fullPension.value?.replace(",", "")?.toDouble()!!
        val cf = commutationFactor.value?.toDouble()!!
        // If the fund value is null or empty, this becomes 0.0
        val dc = if (dcFund.value.equals("")) 0.0 else dcFund.value
            ?.replace(",", "")
            ?.toDouble() ?: 0.0
        val sLta = standardLta.value?.replace(",", "")?.toDouble()!!

        if (fp <= 0 || cf <= 0) {
            return showToastMessage(R.string.pension_cf_zero_toast)
        }

        calculationWrapper(fp, cf, dc, sLta)
    }

    private fun calculationWrapper(fp: Double, cf: Double, dc: Double, sLta: Double) {
        // Call function to calculate db benefits, and return a data object with the results
        dbBenefits = dbPclsCalculation(fp, cf, sLta, dc)
        // The benefits where no pcls is calculated only need access to the Lta calculation.
        noPclsBenefits = Benefits(
            "£0.00",
            formatAsCurrency(fp.toBigDecimal()),
            ltaCalculation(0.0, fp, dc, sLta),
            formatAsCurrency(dc.toBigDecimal())
        )

        // Only enters arguments into the combined
        // pcls function if there is any money purchase fund value, otherwise get the default
        // using £0.00 figures
        cmbBenefits1 = if (dc > 0.0) {
            if (dc >= ((fp * 20) / 3)) {
                largeCmbPclsCalculation(fp, cf, true, sLta, dc)
            } else {
                smallCmbPclsCalculation(fp, cf, sLta, dc)
            }
        } else {
            Benefits("£0.00", "£0.00", "0.00%", "£0.00")
        }

        // These benefits are for where there is a large DC fund and the residual amount is
        // taken as a UFPLS
        cmbBenefits2 = if (dc >= ((fp * 20) / 3))
            largeCmbPclsCalculation(fp, cf, false, sLta, dc) else Benefits("£0.00",
        "£0.00", "0.00%", "£0.00")



        updateWithResults(dbBenefits, cmbBenefits1, cmbBenefits2, noPclsBenefits)
    }

    private fun updateWithResults(dbBenefits: Benefits, cmbBenefits1: Benefits?,
                                  cmbBenefits2: Benefits?, noPclsBenefits: Benefits
    ) {
        Timber.w("Checking that update with results works!")
        _dbBenOutput.value = dbBenefits
        cmbBenefits1?.let {
            _cmbBenOutput1.value = it
        }
        cmbBenefits2?.let {
            _cmbBenOutput2.value = it
        }
        _noPclsBenOutput.value = noPclsBenefits
    }

    fun showToastMessage(message: Int) {
        _toastText.value = Event(message)
    }

}