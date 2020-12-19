package com.bmstu.shatnyuk.androidrk1

import android.util.Log
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
        if (baseAsset.value == null) {
            baseAsset.postValue(asset)
        } else {
            if (baseAsset.value != asset) {
                baseAsset.postValue(asset)
            }
        }
    }
}