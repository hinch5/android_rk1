package com.bmstu.shatnyuk.androidrk1.model

class MarketData(
    val openTime: Long,
    val openPrice: String,
    val highPrice: String,
    val lowPrice: String,
    val closePrice: String,
    val volume: String,
    val closeTime: Long,
    val quoteVolume: String,
    val numberOfTrades: Long
)