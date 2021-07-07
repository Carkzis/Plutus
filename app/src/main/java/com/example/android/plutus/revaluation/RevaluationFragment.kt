package com.example.android.plutus.revaluation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.plutus.databinding.FragmentRevaluationBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RevaluationFragment : Fragment() {

    private val viewModel by viewModels<RevaluationViewModel>()

    private lateinit var viewDataBinding: FragmentRevaluationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentRevaluationBinding.inflate(inflater, container, false).apply {
            revaluationViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButton()
        setUpLiveDataObservers()

    }

    private fun setUpButton() {
        viewDataBinding.calculateRevalButton.setOnClickListener {
            viewModel.showIt()
        }
    }

    private fun setUpLiveDataObservers() {
        viewModel.cpiPercentages.observe(viewLifecycleOwner, {})
        viewModel.rpiPercentages.observe(viewLifecycleOwner, {})
    }

}