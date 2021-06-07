package com.example.android.plutus

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CpiAdapter : RecyclerView.Adapter<ViewHolder>() {

    var data = listOf<InflationRate>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.inflation_rate_item, parent, false) as TextView
        return ViewHolder(view)
    }



}

class ViewHolder constructor(val textView: TextView): RecyclerView.ViewHolder(textView)