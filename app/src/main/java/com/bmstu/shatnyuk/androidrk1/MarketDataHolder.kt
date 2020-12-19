package com.bmstu.shatnyuk.androidrk1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MarketDataHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val baseAsset = view.findViewById<TextView>(R.id.base_asset)
    private val price = view.findViewById<TextView>(R.id.price)
    private val quoteAsset = view.findViewById<TextView>(R.id.quote_asset)
    private val date = view.findViewById<TextView>(R.id.date)
    private lateinit var data: MarketData

    fun setData(marketData: MarketData) {
        baseAsset.text = marketData.baseAsset
        val dec = DecimalFormat("#.##")
        price.text = dec.format(marketData.closePrice.toDouble())
        quoteAsset.text = marketData.quoteAsset
        val closeDate = Date(marketData.closeTime)
        val fromat = SimpleDateFormat("yyyy-MM-dd")
        date.text = fromat.format(closeDate).toString()
        data = marketData
    }

    fun setOnclick(
        hostFragment: HostFragment,
        navToDetailsFn: (fragmentContext: HostFragment, data: MarketData) -> Unit
    ) {
        view.setOnClickListener { navToDetailsFn(hostFragment, data) }
    }
}