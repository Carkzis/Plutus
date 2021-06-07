package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentCpiInflationBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class CpiInflationFragment : Fragment() {

    private val viewModel by viewModels<CpiInflationViewModel>()

    private lateinit var viewDataBinding: FragmentCpiInflationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
                FragmentCpiInflationBinding.inflate(inflater, container, false).apply {
                    viewmodel = viewModel
                }

        viewDataBinding.lifecycleOwner = this

        viewDataBinding.cpiRecyclerview.adapter = CpiAdapter()

        return viewDataBinding.root
    }

}