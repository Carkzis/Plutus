package com.carkzis.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.carkzis.android.plutus.RpiPercentage
import com.carkzis.android.plutus.databinding.FragmentRpiInflationBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * This displays CPI 12-month percentage data to the user.
 */
@AndroidEntryPoint
class RpiPctFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<RpiPctViewModel>()
    private lateinit var viewDataBinding: FragmentRpiInflationBinding
    private lateinit var rpiPctAdapter: RpiPctAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
        Set up data binding between the fragment and the layout.
        */
        viewDataBinding =
            FragmentRpiInflationBinding.inflate(inflater, container, false).apply {
                rpiInflationViewModel = viewModel
            }

        rpiPctAdapter = RpiPctAdapter()
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewDataBinding.rpiRecyclerview.adapter = rpiPctAdapter

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
        viewDataBinding.rpiPctSearchview.setOnQueryTextListener(this)
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
        viewModel.inflationRates.observe(viewLifecycleOwner, Observer<List<RpiPercentage>> {
            rpiPctAdapter.addItemsToAdapter(it)
            onQueryTextSubmit("")
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        rpiPctAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        rpiPctAdapter.filter.filter(newText)
        return false
    }

}