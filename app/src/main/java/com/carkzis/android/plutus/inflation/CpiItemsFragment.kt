package com.carkzis.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.carkzis.android.plutus.CpiItem
import com.carkzis.android.plutus.databinding.FragmentCpiItemsBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import timber.log.Timber

/**
 * This displays CPI inflation data to the user.
 */
@AndroidEntryPoint
class CpiItemsFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<CpiItemsViewModel>()
    private lateinit var viewDataBinding: FragmentCpiItemsBinding
    private lateinit var cpiItemsAdapter: CpiItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
        Set up data binding between the fragment and the layout.
        */
        viewDataBinding =
            FragmentCpiItemsBinding.inflate(inflater, container, false).apply {
                cpiItemsViewModel = viewModel
            }

        cpiItemsAdapter = CpiItemsAdapter()
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewDataBinding.cpiItemsRecyclerview.adapter = cpiItemsAdapter

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
        viewDataBinding.cpiSearchview.setOnQueryTextListener(this)
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
            cpiItemsAdapter.addItemsToAdapter(it)
            onQueryTextSubmit("")
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        cpiItemsAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        cpiItemsAdapter.filter.filter(newText)
        return false
    }

}