package com.ukadovlad21.mvvm1.repository

import androidx.lifecycle.LiveData
import com.ukadovlad21.mvvm1.api.RetrofitInstance
import com.ukadovlad21.mvvm1.db.CurrencyDatabase
import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.models.fromNameToName.CurrencyFromNameToName
import retrofit2.Response

class CurrencyRepository(
    val db: CurrencyDatabase
) {
    suspend fun getCurrency(base: String): Response<CurrenciesLatest> =
        RetrofitInstance.api.getLatestCurrency(base)

    suspend fun getCurrencyNameToName(
        fromName: String,
        toName: String,
        amount: Int
    ): Response<CurrencyFromNameToName> =
        RetrofitInstance.api.getFromNameToName(fromName, toName, amount.toString())


    suspend fun upsert(currencyNameAndPrice: CurrencyNameAndPrice): Long =
        db.getCurrencyDao().upsert(currencyNameAndPrice)

    fun getSavedCurrenciesLiveData(): LiveData<List<CurrencyNameAndPrice>> =
        db.getCurrencyDao().getAllSavedCurrenciesLiveData()

    suspend fun getSavedCurrencies(): List<CurrencyNameAndPrice> =
        db.getCurrencyDao().getAllSavedCurrencies()

    suspend fun deleteCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Unit =
        db.getCurrencyDao().deleteCurrency(currencyNameAndPrice)

    suspend fun updateCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Unit =
        db.getCurrencyDao().updateCurrency(
            currencyNameAndPrice.name,
            currencyNameAndPrice.price,
            currencyNameAndPrice.actualAt
        )
}
