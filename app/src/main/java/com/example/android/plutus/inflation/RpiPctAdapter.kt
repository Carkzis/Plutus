package com.example.android.plutus.inflation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.RpiPercentage
import com.example.android.plutus.databinding.RpiInflationRateItemBinding

class RpiAdapter : ListAdapter<RpiPercentage, RpiAdapter.RpiViewHolder>(RpiDiffCallBack()) {

    override fun onBindViewHolder(holder: RpiViewHolder, position: Int) {
        val rpiItem = getItem(position)
        holder.bind(rpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RpiViewHolder {
        return RpiViewHolder.from(parent)
    }

    class RpiViewHolder constructor(private var binding: RpiInflationRateItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(percentage: RpiPercentage) {
            binding.rpiRate = percentage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RpiViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RpiInflationRateItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return RpiViewHolder(binding)
            }
        }
    }

}

class RpiDiffCallBack : DiffUtil.ItemCallback<RpiPercentage>() {
    override fun areItemsTheSame(oldItem: RpiPercentage, newItem: RpiPercentage): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: RpiPercentage, newItem: RpiPercentage): Boolean {
        return oldItem == newItem
    }
}