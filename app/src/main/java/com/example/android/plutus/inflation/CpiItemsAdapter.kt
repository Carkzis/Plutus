package com.example.android.plutus.inflation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.plutus.CpiItem
import com.example.android.plutus.CpiPercentage
import com.example.android.plutus.RpiPercentage
import com.example.android.plutus.databinding.CpiInflationRateItemBinding
import com.example.android.plutus.databinding.CpiItemBinding
import com.example.android.plutus.databinding.RpiInflationRateItemBinding
import timber.log.Timber

class CpiItemsAdapter : ListAdapter<CpiItem, CpiItemsAdapter.CpiItemsViewHolder>(CpiItemsDiffCallBack()), Filterable {

    var cpiItemList : ArrayList<CpiItem> = ArrayList()
    var cpiItemListFiltered : ArrayList<CpiItem> = ArrayList()

    override fun onBindViewHolder(holder: CpiItemsViewHolder, position: Int) {
        holder.bind(cpiItemListFiltered[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiItemsViewHolder {
        return CpiItemsViewHolder.from(parent)
    }

    override fun getItemCount(): Int = cpiItemListFiltered.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItemsToAdapter(items: List<CpiItem>) {
        cpiItemList = items as ArrayList<CpiItem>
        cpiItemList.reverse()
        cpiItemListFiltered = cpiItemList
        notifyDataSetChanged()
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
                return CpiItemsViewHolder(binding)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) {
                    cpiItemListFiltered = ArrayList<CpiItem>()
                } else {
                    val filteredList = ArrayList<CpiItem>()
                    cpiItemList.filter {
                        it.month.contains(constraint!!) || it.year.contains(constraint) ||
                                it.date.contains(constraint)
                    }.forEach {
                        filteredList.add(it)
                    }
                    cpiItemListFiltered = filteredList
                }

                return FilterResults().apply { values = cpiItemListFiltered }

            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                cpiItemListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<CpiItem>
                }
                Timber.e(cpiItemListFiltered.toString())
                notifyDataSetChanged()
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