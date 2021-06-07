package com.example.android.plutus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CpiAdapter : ListAdapter<InflationRate, ViewHolder>(CpiDiffCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView.text = item.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.inflation_rate_item, parent, false) as TextView
        return ViewHolder(view)
    }

}

class ViewHolder constructor(val textView: TextView): RecyclerView.ViewHolder(textView)

class CpiDiffCallBack : DiffUtil.ItemCallback<InflationRate>() {
    override fun areItemsTheSame(oldItem: InflationRate, newItem: InflationRate): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: InflationRate, newItem: InflationRate): Boolean {
        return oldItem == newItem
    }
}