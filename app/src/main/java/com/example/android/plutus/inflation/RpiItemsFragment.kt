package com.example.android.plutus.inflation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.plutus.RpiItem
import com.example.android.plutus.databinding.FragmentRpiItemsBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RpiItemsFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModels<RpiItemsViewModel>()

    private lateinit var viewDataBinding: FragmentRpiItemsBinding

    private lateinit var rpiItemsAdapter: RpiItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding =
                FragmentRpiItemsBinding.inflate(inflater, container, false).apply {
                    rpiItemsViewModel = viewModel
                }

        rpiItemsAdapter = RpiItemsAdapter()

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewDataBinding.rpiItemsRecyclerview.adapter = rpiItemsAdapter

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToast()
        setUpSearchView()
        setUpDataObserver()
    }

    private fun setUpSearchView() {
        viewDataBinding.rpiSearchview.setOnQueryTextListener(this)
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

    private fun setUpDataObserver() {
        viewModel.inflationRates.observe(viewLifecycleOwner, Observer<List<RpiItem>> {
            rpiItemsAdapter.addItemsToAdapter(it)
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        rpiItemsAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        rpiItemsAdapter.filter.filter(newText)
        return false
    }

}