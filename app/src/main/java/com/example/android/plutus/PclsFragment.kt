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
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpButton()
        setUpToast()

    }

    private fun setUpButton() {
        viewDataBinding.calculatePclsButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            context?.showToast(it)
        })
    }

}