package com.example.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.R
import com.example.android.plutus.databinding.FragmentRpiItemsBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
class RpiItemsFragment : Fragment() {

    private val viewModel by viewModels<RpiItemsViewModel>()

    private lateinit var viewDataBinding: FragmentRpiItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
                FragmentRpiItemsBinding.inflate(inflater, container, false).apply {
                    rpiItemsViewModel = viewModel
                }

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.rpiItemsRecyclerview.adapter = RpiItemsAdapter()

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