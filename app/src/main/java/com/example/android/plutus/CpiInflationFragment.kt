package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentCpiInflationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CpiInflationFragment : Fragment() {

    private val viewModel by viewModels<CpiInflationViewModel> {
        CpiInflationViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentCpiInflationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
                FragmentCpiInflationBinding.inflate(inflater, container, false).apply {
                    viewmodel = viewModel
                }

        return viewDataBinding.root
    }

}