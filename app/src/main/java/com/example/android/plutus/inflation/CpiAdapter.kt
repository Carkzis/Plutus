package com.example.android.plutus.inflation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.databinding.CpiInflationRateItemBinding

class CpiAdapter : ListAdapter<CpiPercentage, CpiAdapter.CpiViewHolder>(CpiDiffCallBack()) {

    override fun onBindViewHolder(holder: CpiViewHolder, position: Int) {
        val cpiItem = getItem(position)
        holder.bind(cpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiViewHolder {
        return CpiViewHolder.from(parent)
    }

    class CpiViewHolder constructor(private var binding: CpiInflationRateItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cpiPercentage: CpiPercentage) {
            binding.cpiRate = cpiPercentage
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

class CpiDiffCallBack : DiffUtil.ItemCallback<CpiPercentage>() {
    override fun areItemsTheSame(oldItem: CpiPercentage, newItem: CpiPercentage): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: CpiPercentage, newItem: CpiPercentage): Boolean {
        return oldItem == newItem
    }
}