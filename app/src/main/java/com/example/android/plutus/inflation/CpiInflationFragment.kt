package com.example.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.CpiInflationViewModel
import com.example.android.plutus.databinding.FragmentCpiInflationBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint

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
                    cpiInflationViewModel = viewModel
                }

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.cpiRecyclerview.adapter = CpiAdapter()

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToast()
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

}