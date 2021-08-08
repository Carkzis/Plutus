package com.example.android.plutus.inflation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.plutus.CpiItem
import com.example.android.plutus.R
import com.example.android.plutus.databinding.FragmentCpiItemsBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CpiItemsFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<CpiItemsViewModel>()

    private lateinit var viewDataBinding: FragmentCpiItemsBinding

    private lateinit var cpiItemsAdapter: CpiItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
            FragmentCpiItemsBinding.inflate(inflater, container, false).apply {
                cpiItemsViewModel = viewModel
            }

        cpiItemsAdapter = CpiItemsAdapter()

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.cpiItemsRecyclerview.adapter = cpiItemsAdapter

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToast()
        setUpSearchView()
        setUpDataObserver()
    }

    private fun setUpSearchView() {
        viewDataBinding.cpiSearchview.setOnQueryTextListener(this)
    }


    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

    private fun setUpDataObserver() {
        viewModel.inflationRates.observe(viewLifecycleOwner, Observer<List<CpiItem>> {
            cpiItemsAdapter.addItemsToAdapter(it)
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