package com.example.android.plutus.inflation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.CpiItem
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage
import com.example.android.plutus.databinding.CpiInflationRateItemBinding
import com.example.android.plutus.databinding.CpiItemBinding
import com.example.android.plutus.databinding.RpiInflationRateItemBinding

class CpiItemsAdapter : ListAdapter<CpiItem, CpiItemsAdapter.CpiItemsViewHolder>(CpiItemsDiffCallBack()) {

    override fun onBindViewHolder(holder: CpiItemsAdapter.CpiItemsViewHolder, position: Int) {
        val cpiItem = getItem(position)
        holder.bind(cpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiItemsAdapter.CpiItemsViewHolder {
        return CpiItemsAdapter.CpiItemsViewHolder.from(parent)
    }

    class CpiItemsViewHolder constructor(private var binding: CpiItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CpiItem) {
            binding.cpiItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CpiItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CpiItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return CpiItemsAdapter.CpiItemsViewHolder(binding)
            }
        }
    }

}

class CpiItemsDiffCallBack : DiffUtil.ItemCallback<CpiItem>() {
    override fun areItemsTheSame(oldItem: CpiItem, newItem: CpiItem): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: CpiItem, newItem: CpiItem): Boolean {
        return oldItem == newItem
    }
}