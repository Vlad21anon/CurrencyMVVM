package com.ukadovlad21.mvvm1.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ukadovlad21.mvvm1.repository.CurrencyRepository

class CurrencyViewModelProviderFactory(
    val app: Application,
    private val currencyRepository: CurrencyRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyViewModel(app, currencyRepository, CheckInternetStateUseCase(app), ResponseMapper(currencyRepository)) as T
    }
}
