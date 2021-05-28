package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.plutus.databinding.FragmentPclsBinding
import timber.log.Timber

class PclsFragment : Fragment() {

    private val viewModel by viewModels<PclsViewModel> {
        PclsViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentPclsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentPclsBinding.inflate(inflater, container, false).apply {
            pclsViewModel = viewModel
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
        // Listener for the results of the DB benefits
        viewModel.dbBenOutput.observe(viewLifecycleOwner, {
            viewDataBinding.dbOnlyLinearLayout.visibility = View.VISIBLE
            if (it.dcFund != "£0.00") {
                viewDataBinding.dbDcText.visibility = View.VISIBLE
                viewDataBinding.dbDcResultText.visibility = View.VISIBLE
            } else {
                viewDataBinding.dbDcText.visibility = View.GONE
                viewDataBinding.dbDcResultText.visibility = View.GONE
            }
        })

        // Listener for the results of the combined benefits (if applicable)
        viewModel.cmbBenOutput.observe(viewLifecycleOwner, {
            it?.let {
                if (it.pcls != "£0.00") {
                    viewDataBinding.combinedLinearLayout.visibility = View.VISIBLE
                } else {
                    viewDataBinding.combinedLinearLayout.visibility = View.GONE
                }
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