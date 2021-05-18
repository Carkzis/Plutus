package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.plutus.databinding.FragmentContentsBinding


class ContentsFragment : Fragment() {

    private val viewModel by viewModels<ContentsViewModel> {
        ContentsViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentContentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding =
            FragmentContentsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewDataBinding.pclsButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToPclsFragment())
        }
    }

}