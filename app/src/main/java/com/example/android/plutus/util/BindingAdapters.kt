package com.example.android.plutus.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.CpiItem
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiItem
import com.example.android.plutus.RpiPercentage
import com.example.android.plutus.inflation.*

@BindingAdapter("cpiListData")
fun bindCpiRecyclerView(recyclerView: RecyclerView, data: List<CpiPercentage>?) {
    val adapter = recyclerView.adapter as CpiAdapter
    adapter.submitList(data)
}

@BindingAdapter("rpiListData")
fun bindRpiRecyclerView(recyclerView: RecyclerView, data: List<RpiPercentage>?) {
    val adapter = recyclerView.adapter as RpiAdapter
    adapter.submitList(data)
}

//@BindingAdapter("cpiItemListData")
//fun bindCpiItemsRecyclerView(recyclerView: RecyclerView, data: List<CpiItem>?) {
//    val adapter = recyclerView.adapter as CpiItemsAdapter
//    adapter.submitList(data)
//}

@BindingAdapter("rpiItemListData")
fun bindRpiItemsRecyclerView(recyclerView: RecyclerView, data: List<RpiItem>?) {
    val adapter = recyclerView.adapter as RpiItemsAdapter
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