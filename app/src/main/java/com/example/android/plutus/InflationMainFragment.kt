package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.plutus.databinding.FragmentInflationMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InflationMainFragment : Fragment() {

    private val viewModel by viewModels<InflationMainViewModel>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()

    }

    private fun setupNavigation() {
        viewDataBinding.cpiButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections
                .actionInflationMainFragmentToCpiInflationFragment())
        }
    }

}