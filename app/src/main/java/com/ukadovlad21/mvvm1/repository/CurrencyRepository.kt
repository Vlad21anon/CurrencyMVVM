package com.ukadovlad21.mvvm1.repository

import com.ukadovlad21.mvvm1.api.RetrofitInstance
import com.ukadovlad21.mvvm1.db.CurrencyDatabase
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice

class CurrencyRepository(
    val db: CurrencyDatabase
) {
    suspend fun getCurrency(base:String) =
        RetrofitInstance.api.getLatestCurrency(base)
    suspend fun getCurrencyNameToName(fromName:String,toName:String,amount:Int) =
        RetrofitInstance.api.getFromNameToName(fromName,toName,amount.toString())


    suspend fun upsert(currencyNameAndPrice: CurrencyNameAndPrice)
        = db.getCurrencyDao().upsert(currencyNameAndPrice)
    fun getSavedCurrencies()
        = db.getCurrencyDao().getAllSavedCurrencies()
    suspend fun deleteCurrency(currencyNameAndPrice: CurrencyNameAndPrice)
        = db.getCurrencyDao().deleteCurrency(currencyNameAndPrice)
    suspend fun updateCurrency(currencyNameAndPrice: CurrencyNameAndPrice)
            = db.getCurrencyDao().updateCurrency(
                    currencyNameAndPrice.name,
                    currencyNameAndPrice.price,
                    currencyNameAndPrice.actualAt
    )
}
