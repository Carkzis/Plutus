package com.example.android.plutus.pcls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentPclsCalcBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PclsCalcFragment : Fragment() {

    private val viewModel by viewModels<PclsCalcViewModel>()

    private lateinit var viewDataBinding: FragmentPclsCalcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentPclsCalcBinding.inflate(inflater, container, false).apply {
            pclsCalcViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButton()
        setUpToast()
        setUpBenefitResultsListeners()

    }

    private fun setUpBenefitResultsListeners() {
        // Listener for the results of the combined benefits (if applicable)
        viewModel.cmbBenOutput1.observe(viewLifecycleOwner, {
            it?.let {
                if (it.pcls != "£0.00") {
                    viewDataBinding.opt1Table.visibility = View.VISIBLE
                    if (it.dcFund != "£0.00") {
                        viewDataBinding.opt1CmbDc.visibility = View.VISIBLE
                    } else {
                        viewDataBinding.opt1CmbDc.visibility = View.GONE
                    }
                } else {
                    viewDataBinding.opt1Table.visibility = View.GONE
                }
            }
        })

        viewModel.cmbBenOutput2.observe(viewLifecycleOwner, {
            it?.let {
                if (it.pcls != "£0.00" && it.dcFund != "£0.00") {
                    viewDataBinding.opt1bTable.visibility = View.VISIBLE
                } else {
                    viewDataBinding.opt1bTable.visibility = View.GONE
                }
            }
        })

        // Listener for the results of the DB benefits
        viewModel.dbBenOutput.observe(viewLifecycleOwner, {
            viewDataBinding.opt2Table.visibility = View.VISIBLE
            if (it.dcFund != "£0.00") {
                viewDataBinding.opt2DbDc.visibility = View.VISIBLE
            } else {
                viewDataBinding.opt2DbDc.visibility = View.GONE
            }
        })

        viewModel.noPclsBenOutput.observe(viewLifecycleOwner, {
            viewDataBinding.opt3Table.visibility = View.VISIBLE
            if (it.dcFund != "£0.00") {
                viewDataBinding.opt3NpDc.visibility = View.VISIBLE
            } else {
                viewDataBinding.opt3NpDc.visibility = View.GONE
            }
        })
    }

    private fun setUpButton() {
        viewDataBinding.calculatePclsButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

}