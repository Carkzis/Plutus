package com.carkzis.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.carkzis.android.plutus.CpiPctViewModel
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.databinding.FragmentCpiInflationBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * This displays CPI 12-month percentage data to the user.
 */
@AndroidEntryPoint
class CpiPctFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<CpiPctViewModel>()
    private lateinit var viewDataBinding: FragmentCpiInflationBinding
    private lateinit var cpiPctAdapter: CpiPctAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
        Set up data binding between the fragment and the layout.
        */
        viewDataBinding =
                FragmentCpiInflationBinding.inflate(inflater, container, false).apply {
                    cpiInflationViewModel = viewModel
                }

        cpiPctAdapter = CpiPctAdapter()
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewDataBinding.cpiRecyclerview.adapter = cpiPctAdapter

        return viewDataBinding.root
    }

    /**
     * Used here to set up various observers/listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToast()
        setUpSearchView()
        setUpDataObserver()
    }

    /**
     * Set up the search view, allowing the user to search the inflation data by month,
     * year and date.
     */
    private fun setUpSearchView() {
        viewDataBinding.cpiPctSearchview.setOnQueryTextListener(this)
    }

    /**
     * Sets up the ability to show a toast once by observing the LiveData in the ViewModel.
     */
    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

    /**
     * Sets up the initial observation of the data, and adds it to the RecyclerView adapter.
     * It submits a "" query, which will return everything.
     */
    private fun setUpDataObserver() {
        viewModel.inflationRates.observe(viewLifecycleOwner, {
            cpiPctAdapter.addItemsToAdapter(it)
            onQueryTextSubmit("")
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        cpiPctAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        cpiPctAdapter.filter.filter(newText)
        return false
    }

}