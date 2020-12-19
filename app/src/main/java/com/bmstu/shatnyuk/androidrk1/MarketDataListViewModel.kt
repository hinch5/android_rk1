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

    private val refreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getMarketDataList(): LiveData<List<MarketData>> {
        return marketDataList
    }

    fun getRefreshing(): LiveData<Boolean> {
        return refreshing
    }

    fun refreshMarketData(baseAsset: String, quoteAsset: String, numberOfDays: Int) {
        GlobalScope.launch {
            refreshing.postValue(true)
            val marketDataListFetched = loadData(baseAsset, quoteAsset, numberOfDays)
            marketDataList.postValue(marketDataListFetched)
            refreshing.postValue(false)
        }
    }

    private suspend fun loadData(
        baseAsset: String,
        quoteAsset: String,
        numberOfDays: Int
    ): List<MarketData> {
        return loadMarketData(baseAsset, quoteAsset, numberOfDays)
    }
}