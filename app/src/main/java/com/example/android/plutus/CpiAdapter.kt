package com.example.android.plutus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.databinding.InflationRateItemBinding

class CpiAdapter : ListAdapter<InflationRate, CpiAdapter.CpiViewHolder>(CpiDiffCallBack()) {

    override fun onBindViewHolder(holder: CpiViewHolder, position: Int) {
        val cpiItem = getItem(position)
        holder.bind(cpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiViewHolder {
        return CpiViewHolder(InflationRateItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class CpiViewHolder constructor(private var binding: InflationRateItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(inflationRate: InflationRate) {
                binding.cpiRate = inflationRate
                binding.executePendingBindings()
            }
    }

}

class CpiDiffCallBack : DiffUtil.ItemCallback<InflationRate>() {
    override fun areItemsTheSame(oldItem: InflationRate, newItem: InflationRate): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: InflationRate, newItem: InflationRate): Boolean {
        return oldItem == newItem
    }
}