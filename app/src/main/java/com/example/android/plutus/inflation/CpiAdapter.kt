package com.example.android.plutus.inflation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.CpiInflationRate
import com.example.android.plutus.databinding.CpiInflationRateItemBinding

class CpiAdapter : ListAdapter<CpiInflationRate, CpiAdapter.CpiViewHolder>(CpiDiffCallBack()) {

    override fun onBindViewHolder(holder: CpiViewHolder, position: Int) {
        val cpiItem = getItem(position)
        holder.bind(cpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiViewHolder {
        return CpiViewHolder.from(parent)
    }

    class CpiViewHolder constructor(private var binding: CpiInflationRateItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cpiInflationRate: CpiInflationRate) {
            binding.cpiRate = cpiInflationRate
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CpiViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CpiInflationRateItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return CpiViewHolder(binding)
            }
        }
    }

}

class CpiDiffCallBack : DiffUtil.ItemCallback<CpiInflationRate>() {
    override fun areItemsTheSame(oldItem: CpiInflationRate, newItem: CpiInflationRate): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: CpiInflationRate, newItem: CpiInflationRate): Boolean {
        return oldItem == newItem
    }
}