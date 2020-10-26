package com.bmstu.shatnyuk.androidrk1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseQuoteViewModel : ViewModel() {
    private val baseAsset: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getBaseAsset(): LiveData<String> {
        return baseAsset
    }

    fun baseAssetInput(asset: String) {
        baseAsset.postValue(asset)
    }
}