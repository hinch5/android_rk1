package com.bmstu.shatnyuk.androidrk1

import android.util.Log
import com.bmstu.shatnyuk.androidrk1.model.MarketData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Long

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

public fun loadTrades(numberOfDays: Int) = runBlocking<List<MarketData>> {
    try {
        val historyRaw = MarketApi.retrofitService.history(
            "BTCUSDT",
            "1d",
            numberOfDays
        )
        List<MarketData>(historyRaw.size) { index ->
            val historyElem = historyRaw[index]
            MarketData(
                Long.parseLong(historyElem[0]),
                historyElem[1],
                historyElem[2],
                historyElem[3],
                historyElem[4],
                historyElem[5],
                Long.parseLong(historyElem[6]),
                historyElem[7],
                Long.parseLong(historyElem[8])
            )
        }
    } catch (e: Exception) {
        Log.e("Market api", "Api error: ${e.message}, $e")
        throw e
    }
}