package com.carkzis.android.plutus.inflation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carkzis.android.plutus.RpiPercentage
import com.carkzis.android.plutus.databinding.RpiInflationRateItemBinding
import timber.log.Timber

/**
 * This is a RecyclerView adapter for binding RPI percentage data, and allowing the data to be searched.
 */
class RpiPctAdapter : ListAdapter<RpiPercentage, RpiPctAdapter.RpiViewHolder>(RpiDiffCallBack()), Filterable {

    var rpiPercentageList : ArrayList<RpiPercentage> = ArrayList()
    var rpiPercentageListFiltered : ArrayList<RpiPercentage> = ArrayList()

    override fun onBindViewHolder(holder: RpiViewHolder, position: Int) {
        holder.bind(rpiPercentageListFiltered[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RpiViewHolder {
        return RpiViewHolder.from(parent)
    }

    override fun getItemCount(): Int = rpiPercentageListFiltered.size

    /**
     * This adds the data to the two lists, which will initially be the same.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsToAdapter(items: List<RpiPercentage>) {
        rpiPercentageList = items as ArrayList<RpiPercentage>
        rpiPercentageListFiltered = rpiPercentageList
        notifyDataSetChanged()
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) {
                    // If the search string is empty, we show all the items (the default).
                    rpiPercentageListFiltered = rpiPercentageList
                } else {
                    // We get a new empty list, and add all the filtered data to this list.
                    val filteredList = ArrayList<RpiPercentage>()
                    rpiPercentageList.filter {
                        it.month.lowercase().contains(constraint!!.toString().lowercase())
                                || it.year.contains(constraint) || it.date.contains(constraint)
                    }.forEach {
                        filteredList.add(it)
                    }
                    rpiPercentageListFiltered = filteredList
                }

                return FilterResults().apply { values = rpiPercentageListFiltered }

            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                rpiPercentageListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<RpiPercentage>
                }
                Timber.e(rpiPercentageListFiltered.toString())
                notifyDataSetChanged()
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