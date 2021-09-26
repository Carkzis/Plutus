package com.carkzis.android.plutus.inflation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.databinding.CpiInflationRateItemBinding
import timber.log.Timber

/**
 * This is a RecyclerView adapter for binding CPI percentage data, and allowing the data to be searched.
 */
class CpiPctAdapter : ListAdapter<CpiPercentage, CpiPctAdapter.CpiViewHolder>(CpiDiffCallBack()),
    Filterable {

    var cpiPercentageList : ArrayList<CpiPercentage> = ArrayList()
    var cpiPercentageListFiltered : ArrayList<CpiPercentage> = ArrayList()

    override fun onBindViewHolder(holder: CpiViewHolder, position: Int) {
        holder.bind(cpiPercentageListFiltered[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpiViewHolder {
        return CpiViewHolder.from(parent)
    }

    override fun getItemCount(): Int = cpiPercentageListFiltered.size

    /**
     * This adds the data to the two lists, which will initially be the same.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsToAdapter(items: List<CpiPercentage>) {
        cpiPercentageList = items as ArrayList<CpiPercentage>
        cpiPercentageListFiltered = cpiPercentageList
        notifyDataSetChanged()
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) {
                    // If the search string is empty, we show all the items (the default).
                    cpiPercentageListFiltered = cpiPercentageList
                } else {
                    // We get a new empty list, and add all the filtered data to this list.
                    val filteredList = ArrayList<CpiPercentage>()
                    cpiPercentageList.filter {
                        it.month.lowercase().contains(constraint!!.toString().lowercase())
                                || it.year.contains(constraint) || it.date.contains(constraint)
                    }.forEach {
                        filteredList.add(it)
                    }
                    cpiPercentageListFiltered = filteredList
                }
                return FilterResults().apply { values = cpiPercentageListFiltered }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                cpiPercentageListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<CpiPercentage>
                }
                Timber.e(cpiPercentageListFiltered.toString())
                notifyDataSetChanged()
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