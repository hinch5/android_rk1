package com.bmstu.shatnyuk.androidrk1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MarketDataListViewModel : ViewModel() {

    private val marketDataList: MutableLiveData<List<MarketData>> by lazy {
        MutableLiveData<List<MarketData>>()
    }

    fun getMarketDataList(): LiveData<List<MarketData>> {
        return marketDataList
    }

    fun refreshMarketData(baseAsset: String, quoteAsset: String, numberOfDays: Int) {
        GlobalScope.launch {
            val marketDataListFetched = loadData(baseAsset, quoteAsset, numberOfDays)
            marketDataList.postValue(marketDataListFetched)
        }
    }

    private suspend fun loadData(baseAsset: String, quoteAsset: String, numberOfDays: Int): List<MarketData> {
        return loadMarketData(baseAsset, quoteAsset, numberOfDays)
    }
}