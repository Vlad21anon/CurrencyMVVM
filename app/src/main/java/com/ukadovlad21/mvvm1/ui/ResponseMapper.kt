package com.ukadovlad21.mvvm1.ui

import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import kotlinx.coroutines.runBlocking

internal class ResponseMapper(
    private val currencyRepository: CurrencyRepository,
) {

    internal fun map(
        currenciesLatest: CurrenciesLatest,
    ): List<CurrencyNameAndPrice> {
        val list: MutableList<CurrencyNameAndPrice> = mutableListOf()
        currenciesLatest.rates.forEach { (key, value) ->
            val item = CurrencyNameAndPrice(
                null,
                key,
                value,
                currenciesLatest.date,
                getSavedCurrencies().any { it.name == key }
            )
            list.add(item)
        }
        return list
    }

    private fun getSavedCurrencies(): List<CurrencyNameAndPrice> = runBlocking {
        currencyRepository.getSavedCurrencies()
    }

}
