package com.carkzis.android.plutus.contents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carkzis.android.plutus.databinding.FragmentContentsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying the contents.
 */
@AndroidEntryPoint
class ContentsFragment : Fragment() {

    private val viewModel by viewModels<ContentsViewModel>()
    lateinit var viewDataBinding: FragmentContentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Set up data binding between the fragment and the layout.
        viewDataBinding =
            FragmentContentsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    /**
     * Used here to set up various observers/listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()

    }

    /**
     * Sets up the click listeners to navigate the user to different fragments from the main
     * contents.
     */
    private fun setupNavigation() {
        viewDataBinding.pclsButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToPclsCalcFragment())
        }
        viewDataBinding.dateButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToDateCalcFragment())
        }
        viewDataBinding.inflationButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToInflationMainFragment())
        }
        viewDataBinding.revaluationButton.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToRevaluationFragment())
        }
        viewDataBinding.aboutText.setOnClickListener {
            findNavController().navigate(ContentsFragmentDirections.actionContentsFragmentToAboutFragment())
        }
    }

}