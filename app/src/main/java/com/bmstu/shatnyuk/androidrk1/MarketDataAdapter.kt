package com.bmstu.shatnyuk.androidrk1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmstu.shatnyuk.androidrk1.model.MarketData

class MarketDataAdapter(
    private val hostFragment: HostFragment,
    private val navToDetailsFn: (fragmentContext: HostFragment, data: MarketData) -> Unit
) : RecyclerView.Adapter<MarketDataHolder>() {
    var data = arrayOf<MarketData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketDataHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.market_data_card, parent, false)
        return MarketDataHolder(view)
    }

    override fun onBindViewHolder(holder: MarketDataHolder, position: Int) {
        holder.setData(data[position])
        holder.setOnclick(hostFragment, navToDetailsFn)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}