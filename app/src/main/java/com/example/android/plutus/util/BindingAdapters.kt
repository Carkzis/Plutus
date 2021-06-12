package com.example.android.plutus.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.inflation.CpiAdapter
import com.example.android.plutus.CpiApiLoadingStatus
import com.example.android.plutus.InflationRate

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<InflationRate>?) {
    val adapter = recyclerView.adapter as CpiAdapter
    adapter.submitList(data)
}

@BindingAdapter("loadingStatus")
fun bindLoadingStatus(statusProgressBar: ProgressBar, loadingStatus: CpiApiLoadingStatus) {
    when (loadingStatus) {
        CpiApiLoadingStatus.LOADING ->
            statusProgressBar.visibility = View.VISIBLE
        CpiApiLoadingStatus.ERROR, CpiApiLoadingStatus.DONE ->
            statusProgressBar.visibility = View.GONE
    }
}

@BindingAdapter("errorMessage")
fun bindErrorMessage(errorMessage: TextView, loadingStatus: CpiApiLoadingStatus) {
    when (loadingStatus) {
        CpiApiLoadingStatus.LOADING, CpiApiLoadingStatus.DONE ->
            errorMessage.visibility = View.GONE
        CpiApiLoadingStatus.ERROR ->
            errorMessage.visibility = View.VISIBLE
    }
}