package com.example.android.inflation.plutus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.RpiInflationRate
import com.example.android.plutus.databinding.RpiInflationRateItemBinding

class RpiAdapter : ListAdapter<RpiInflationRate, RpiAdapter.RpiViewHolder>(RpiDiffCallBack()) {

    override fun onBindViewHolder(holder: RpiViewHolder, position: Int) {
        val rpiItem = getItem(position)
        holder.bind(rpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RpiViewHolder {
        return RpiViewHolder.from(parent)
    }

    class RpiViewHolder constructor(private var binding: RpiInflationRateItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(inflationRate: RpiInflationRate) {
            binding.rpiRate = inflationRate
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RpiViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RpiInflationRateItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return RpiAdapter.RpiViewHolder(binding)
            }
        }
    }

}

class RpiDiffCallBack : DiffUtil.ItemCallback<RpiInflationRate>() {
    override fun areItemsTheSame(oldItem: RpiInflationRate, newItem: RpiInflationRate): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: RpiInflationRate, newItem: RpiInflationRate): Boolean {
        return oldItem == newItem
    }
}