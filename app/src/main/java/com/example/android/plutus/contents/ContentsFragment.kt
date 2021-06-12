package com.example.android.plutus.contents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.plutus.databinding.FragmentContentsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsFragment : Fragment() {

    private val viewModel by viewModels<ContentsViewModel>()

    lateinit var viewDataBinding: FragmentContentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
            FragmentContentsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewDataBinding.pclsButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToPclsCalcFragment())
        }
        viewDataBinding.dateButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToDateCalcFragment())
        }
        viewDataBinding.inflationButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToInflationMainFragment())
        }
    }

}