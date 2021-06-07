package com.example.android.plutus

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<InflationRate>?) {
    val adapter = recyclerView.adapter as CpiAdapter
    adapter.submitList(data)
}