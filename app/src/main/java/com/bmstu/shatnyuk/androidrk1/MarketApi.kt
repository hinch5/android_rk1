package com.bmstu.shatnyuk.androidrk1

import android.util.Log
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.binance.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarketApiService {
    @GET("/api/v3/klines")
    suspend fun history(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int
    ): List<List<String>>
}

object MarketApi {
    val retrofitService: MarketApiService by lazy {
        retrofit.create(MarketApiService::class.java)
    }
}

public suspend fun loadMarketData(
    baseAsset: String,
    quoteAsset: String,
    numberOfDays: Int
): List<MarketData> {
    try {
        val historyRaw = MarketApi.retrofitService.history(
            baseAsset + quoteAsset,
            "1d",
            numberOfDays
        )
        Log.i("load market data", "Raw history data: $historyRaw")
        return List<MarketData>(historyRaw.size) { index ->
            val historyElem = historyRaw[index]
            MarketData(
                baseAsset,
                quoteAsset,
                historyElem[0].toLong(),
                historyElem[1],
                historyElem[2],
                historyElem[3],
                historyElem[4],
                historyElem[5],
                historyElem[6].toLong(),
                historyElem[7],
                historyElem[8].toLong()
            )
        }
    } catch (e: Exception) {
        Log.e("Market api", "Api error: ${e.message}, $e")
        throw e
    }
}