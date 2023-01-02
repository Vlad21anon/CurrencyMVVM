package com.ukadovlad21.mvvm1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import kotlinx.coroutines.launch

class SavedCurrencyViewModel(
    private val currencyRepository: CurrencyRepository
):ViewModel() {




    fun getSavedCurrencies() = currencyRepository.getSavedCurrencies()

    fun deleteArticle(currencyNameAndPrice: CurrencyNameAndPrice) = viewModelScope.launch {
        currencyRepository.deleteCurrency(currencyNameAndPrice)
    }


}