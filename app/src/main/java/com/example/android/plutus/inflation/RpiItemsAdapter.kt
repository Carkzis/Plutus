package com.example.android.plutus.inflation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.RpiItem
import com.example.android.plutus.RpiPercentage
import com.example.android.plutus.databinding.RpiInflationRateItemBinding
import com.example.android.plutus.databinding.RpiItemBinding

class RpiItemsAdapter : ListAdapter<RpiItem, RpiItemsAdapter.RpiItemsViewHolder>(RpiItemsDiffCallBack()) {

    override fun onBindViewHolder(holder: RpiItemsViewHolder, position: Int) {
        val rpiItem = getItem(position)
        holder.bind(rpiItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RpiItemsViewHolder {
        return RpiItemsViewHolder.from(parent)
    }

    class RpiItemsViewHolder constructor(private var binding: RpiItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RpiItem) {
            binding.rpiItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RpiItemsAdapter.RpiItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RpiItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return RpiItemsAdapter.RpiItemsViewHolder(binding)
            }
        }
    }

}

class RpiItemsDiffCallBack : DiffUtil.ItemCallback<RpiItem>() {
    override fun areItemsTheSame(oldItem: RpiItem, newItem: RpiItem): Boolean {
        return oldItem.updateDate == newItem.updateDate
    }

    override fun areContentsTheSame(oldItem: RpiItem, newItem: RpiItem): Boolean {
        return oldItem == newItem
    }
}