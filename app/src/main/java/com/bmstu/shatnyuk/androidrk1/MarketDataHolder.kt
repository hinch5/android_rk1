package com.bmstu.shatnyuk.androidrk1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import java.util.*

class MarketDataHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val baseAsset = view.findViewById<TextView>(R.id.base_asset)
    private val price = view.findViewById<TextView>(R.id.price)
    private val quoteAsset = view.findViewById<TextView>(R.id.quote_asset)
    private val date = view.findViewById<TextView>(R.id.date)

    fun setData(marketData: MarketData) {
        baseAsset.text = marketData.baseAsset
        price.text = marketData.closePrice
        quoteAsset.text = marketData.quoteAsset
        val closeDate = Date(marketData.closeTime)
        date.text = closeDate.toString()
    }
}