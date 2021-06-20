package com.example.android.plutus.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.inflation.plutus.RpiAdapter
import com.example.android.plutus.inflation.CpiAdapter
import com.example.android.plutus.inflation.ApiLoadingStatus
import com.example.android.plutus.CpiInflationRate
import com.example.android.plutus.RpiInflationRate

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CpiInflationRate>?) {
    val adapter = recyclerView.adapter as CpiAdapter
    adapter.submitList(data)
}

@BindingAdapter("rpiListData")
fun bindRpiRecyclerView(recyclerView: RecyclerView, data: List<RpiInflationRate>?) {
    val adapter = recyclerView.adapter as RpiAdapter
    adapter.submitList(data)
}

@BindingAdapter("loadingStatus")
fun bindLoadingStatus(statusProgressBar: ProgressBar, loadingStatus: ApiLoadingStatus) {
    when (loadingStatus) {
        ApiLoadingStatus.LOADING ->
            statusProgressBar.visibility = View.VISIBLE
        ApiLoadingStatus.ERROR, ApiLoadingStatus.DONE ->
            statusProgressBar.visibility = View.GONE
    }
}

@BindingAdapter("errorMessage")
fun bindErrorMessage(errorMessage: TextView, loadingStatus: ApiLoadingStatus) {
    when (loadingStatus) {
        ApiLoadingStatus.LOADING, ApiLoadingStatus.DONE ->
            errorMessage.visibility = View.GONE
        ApiLoadingStatus.ERROR ->
            errorMessage.visibility = View.VISIBLE
    }
}