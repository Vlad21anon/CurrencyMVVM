package com.ukadovlad21.mvvm1.repository

import com.ukadovlad21.mvvm1.api.RetrofitInstance
import com.ukadovlad21.mvvm1.db.CurrencyDatabase
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice

class CurrencyRepository(
    val db: CurrencyDatabase
) {
    suspend fun getCurrency(base:String) =
        RetrofitInstance.api.getLatestCurrency("?base=$base")


    suspend fun upsert(currencyNameAndPrice: CurrencyNameAndPrice)
        = db.getCurrencyDao().upsert(currencyNameAndPrice)
    fun getSavedCurrencies()
        = db.getCurrencyDao().getAllSavedCurrencies()
    suspend fun deleteCurrency(currencyNameAndPrice: CurrencyNameAndPrice)
        = db.getCurrencyDao().deleteArticle(currencyNameAndPrice)
}
