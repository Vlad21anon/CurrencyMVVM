package com.ukadovlad21.mvvm1.api

import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.fromNameToName.CurrencyFromNameToName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyAPI {
    @GET("latest")
    suspend fun getLatestCurrency(
        @Query("base") string: String
    ): Response<CurrenciesLatest>

    @GET("convert")
    suspend fun getFromNameToName(
        @Query("from") from:String,
        @Query("to") to:String,
        @Query("amount") amount:String
    ): Response<CurrencyFromNameToName>

}