package com.example.android.plutus.revaluation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentRevaluationBinding
import dagger.hilt.android.AndroidEntryPoint

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

}