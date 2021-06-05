package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentInflationMainBinding

class InflationMainFragment : Fragment() {

    private val viewModel by viewModels<InflationMainViewModel> {
        InflationMainViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentInflationMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
            FragmentInflationMainBinding.inflate(inflater, container, false).apply{
                viewmodel = viewModel
            }

        return viewDataBinding.root
    }

}