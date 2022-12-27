package com.ukadovlad21.mvvm1.api

import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyAPI {
    @GET("latest{base}")
    suspend fun getLatestCurrency(
        @Path("base") string: String
    ): Response<CurrenciesLatest>

}