package com.bmstu.shatnyuk.androidrk1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import kotlinx.coroutines.runBlocking

class MarketDataListViewModel : ViewModel() {

    private val marketDataList: MutableLiveData<List<MarketData>> by lazy {
        MutableLiveData<List<MarketData>>()
    }

    init {
        marketDataList.postValue(loadData())
    }

    fun getMarketDataList(): LiveData<List<MarketData>> {
        return marketDataList
    }

    private fun loadData() = runBlocking<List<MarketData>> {
        loadMarketData("BTC", "USDT", 100)
    }
}