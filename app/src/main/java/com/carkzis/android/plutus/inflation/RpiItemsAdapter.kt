package com.carkzis.android.plutus.inflation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carkzis.android.plutus.RpiItem
import com.carkzis.android.plutus.databinding.RpiItemBinding

class RpiItemsAdapter : ListAdapter<RpiItem, RpiItemsAdapter.RpiItemsViewHolder>(RpiItemsDiffCallBack()),
    Filterable {

    var rpiItemList : ArrayList<RpiItem> = ArrayList()
    var rpiItemListFiltered : ArrayList<RpiItem> = ArrayList()

    override fun onBindViewHolder(holder: RpiItemsViewHolder, position: Int) {
        holder.bind(rpiItemListFiltered[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RpiItemsViewHolder {
        return RpiItemsViewHolder.from(parent)
    }

    override fun getItemCount(): Int = rpiItemListFiltered.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItemsToAdapter(items: List<RpiItem>) {
        rpiItemList = items as ArrayList<RpiItem>
        rpiItemList.reverse()
        rpiItemListFiltered = rpiItemList
        notifyDataSetChanged()
    }

    class RpiItemsViewHolder constructor(private var binding: RpiItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RpiItem) {
            binding.rpiItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RpiItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RpiItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return RpiItemsViewHolder(binding)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) {
                    // If the search string is empty, we show all the items (the default).
                    rpiItemListFiltered = rpiItemList
                } else {
                    val filteredList = ArrayList<RpiItem>()
                    rpiItemList.filter {
                        it.month.lowercase().contains(constraint!!.toString().lowercase()) || it.year.contains(constraint) ||
                                it.date.contains(constraint)
                    }.forEach {
                        filteredList.add(it)
                    }
                    rpiItemListFiltered = filteredList
                }

                return FilterResults().apply { values = rpiItemListFiltered }

            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                rpiItemListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<RpiItem>
                }
                notifyDataSetChanged()
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