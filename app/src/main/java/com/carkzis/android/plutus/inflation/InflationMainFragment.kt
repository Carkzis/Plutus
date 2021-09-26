package com.carkzis.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carkzis.android.plutus.databinding.FragmentInflationMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InflationMainFragment : Fragment() {

    private val viewModel by viewModels<InflationMainViewModel>()
    private lateinit var viewDataBinding: FragmentInflationMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Set up data binding between the fragment and the layout.
        viewDataBinding =
            FragmentInflationMainBinding.inflate(inflater, container, false).apply{
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
        viewDataBinding.cpiButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections.actionInflationMainFragmentToCpiInflationFragment())
        }
        viewDataBinding.rpiButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections.actionInflationMainFragmentToRpiInflationFragment())
        }
        viewDataBinding.rpiItemsButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections.actionInflationMainFragmentToRpiItemsFragment())
        }
        viewDataBinding.gmpFixedButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections.actionInflationMainFragmentToGmpFixedRevalFragment())
        }
        viewDataBinding.cpiItemsButton.setOnClickListener {
            findNavController().navigate(InflationMainFragmentDirections.actionInflationMainFragmentToCpiItemsFragment())
        }
    }

}