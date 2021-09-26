package com.carkzis.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carkzis.android.plutus.databinding.FragmentGmpFixedRevalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GmpFixedRevalFragment : Fragment() {

    private val viewModel by viewModels<GmpFixedRevalViewModel>()

    private lateinit var viewDataBinding: FragmentGmpFixedRevalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Set up data binding between the fragment and the layout.
        viewDataBinding =
            FragmentGmpFixedRevalBinding.inflate(inflater, container, false).apply {
                gmpFixedRevalViewModel = viewModel
            }

        return viewDataBinding.root
    }
}