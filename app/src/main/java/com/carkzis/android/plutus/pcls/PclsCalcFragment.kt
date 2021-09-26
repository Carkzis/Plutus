package com.carkzis.android.plutus.pcls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.databinding.FragmentPclsCalcBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PclsCalcFragment : Fragment() {

    private val viewModel by viewModels<PclsCalcViewModel>()
    private lateinit var viewDataBinding: FragmentPclsCalcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
         Set up data binding between the fragment and the layout. The lifecycleOwner observes
         the changes in LiveData in this databinding.
         */
        viewDataBinding = FragmentPclsCalcBinding.inflate(inflater, container, false).apply {
            pclsCalcViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root
    }

    /**
     * Used here to set up various observers/listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButton()
        setUpToast()
        setUpLtaSpinner()
        setUpBenefitResultsListeners()

    }

    /**
     * Wrapper to set up listeners for the results of the benefits.  Ensures that the titles for
     * each option is appropriately titled and displayed to the UI, and that only the options
     * available when considering the calculation are visible in the UI.
     */
    private fun setUpBenefitResultsListeners() {
        setUpCombinedAndAnnuityListener()
        setUpCombinedAndUfplsListener()
        setUpDbPclsListener()
        setUpNoPclsListener()
    }

    /**
     * Listener for the results of the combined benefits where annuity is taken (if applicable).
     */
    private fun setUpCombinedAndAnnuityListener() {
        viewModel.cmbBenOutput1.observe(viewLifecycleOwner, {
            it?.let {
                if (it.pcls != "£0.00") {
                    // Ensure the non-combined pcls table titles are not set to option 1 or 2.
                    viewDataBinding.opt2Title.text = getText(R.string.opt2_description)
                    viewDataBinding.opt3Title.text = getText(R.string.opt3_description)
                    viewDataBinding.opt1Table.visibility = View.VISIBLE
                    if (it.dcFund != "£0.00") {
                        viewDataBinding.opt1CmbDc.visibility = View.VISIBLE
                        viewDataBinding.opt1Title.text = getText(R.string.opt1a_description)
                    } else {
                        viewDataBinding.opt1CmbDc.visibility = View.GONE
                        viewDataBinding.opt1Title.text = getText(R.string.opt1_description)
                    }
                } else {
                    viewDataBinding.opt1Table.visibility = View.GONE
                    // Need to set the remaining tables to Option 1 and 2 respectively.
                    viewDataBinding.opt2Title.text = getText(R.string.opt1_description)
                    viewDataBinding.opt3Title.text = getText(R.string.opt2_description)
                }
            }
        })
    }

    /**
     * Listener for the results of the combined benefits where UFPLS is taken (if applicable).
     */
    private fun setUpCombinedAndUfplsListener() {
        viewModel.cmbBenOutput2.observe(viewLifecycleOwner, {
            it?.let {
                if (it.pcls != "£0.00" && it.dcFund != "£0.00") {
                    viewDataBinding.opt1bTable.visibility = View.VISIBLE
                } else {
                    viewDataBinding.opt1bTable.visibility = View.GONE
                }
            }
        })
    }

    /**
     * Listener for results of the benefits where a DB PCLS is taken but not combined with DC.
     */
    private fun setUpDbPclsListener() {
        viewModel.dbBenOutput.observe(viewLifecycleOwner, {
            viewDataBinding.opt2Table.visibility = View.VISIBLE
            if (it.dcFund != "£0.00") {
                viewDataBinding.opt2DbDc.visibility = View.VISIBLE
            } else {
                viewDataBinding.opt2DbDc.visibility = View.GONE
            }
        })
    }

    /**
     * Listener for the results of the option where no PCLS is taken.
     */
    private fun setUpNoPclsListener() {
        viewModel.noPclsBenOutput.observe(viewLifecycleOwner, {
            viewDataBinding.opt3Table.visibility = View.VISIBLE
            if (it.dcFund != "£0.00") {
                viewDataBinding.opt3NpDc.visibility = View.VISIBLE
            } else {
                viewDataBinding.opt3NpDc.visibility = View.GONE
            }
        })
    }

    /**
     * Sets up a click listener for the PCLS button.
     */
    private fun setUpButton() {
        viewDataBinding.calculatePclsButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    /**
     * Sets up the ability to show a toast once by observing the LiveData in the ViewModel.
     */
    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

    /**
     * Sets up the LTA spinner that allows the user to choose a standard LTA.
     */
    private fun setUpLtaSpinner() {
        viewModel.spinnerPosition.observe(viewLifecycleOwner, {
            viewModel.setStandardLta(it)
        })
    }

}