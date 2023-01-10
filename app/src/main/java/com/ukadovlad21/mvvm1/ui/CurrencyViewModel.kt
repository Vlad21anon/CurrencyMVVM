package com.ukadovlad21.mvvm1.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.models.fromNameToName.CurrencyFromNameToName
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CurrencyViewModel internal constructor(
    app: Application,
    private val currencyRepository: CurrencyRepository,
    private val checkInternetStateUseCase: CheckInternetStateUseCase,
    private val responseMapper: ResponseMapper,
) : AndroidViewModel(app) {

    private val currencyLiveData: MutableLiveData<Resource<CurrenciesLatest>> = MutableLiveData()

    private val refreshCurrencyLiveData: MutableLiveData<Resource<CurrencyFromNameToName>> =
        MutableLiveData()

    private val convertCurrencyLiveData: MutableLiveData<Resource<CurrencyFromNameToName>> =
        MutableLiveData()


    fun getCurrency(): MutableLiveData<Resource<CurrenciesLatest>> {
        viewModelScope.launch {
            call(currencyLiveData) {
                currencyRepository.getCurrency("usd")
            }
        }

        return currencyLiveData
    }

    fun convertByNames(
        fromName: String, toName: String,
        amount: Int,
    ): MutableLiveData<Resource<CurrencyFromNameToName>> {
        viewModelScope.launch {
            call(convertCurrencyLiveData) {
                currencyRepository.getCurrencyNameToName(fromName, toName, amount)
            }
        }

        return convertCurrencyLiveData
    }

    fun getByNames(
        fromName: String,
        toName: String,
    ): MutableLiveData<Resource<CurrencyFromNameToName>> {
        viewModelScope.launch {
            call(refreshCurrencyLiveData) {
                currencyRepository.getCurrencyNameToName(fromName, toName, 1)
            }
        }

        return refreshCurrencyLiveData
    }

    private suspend fun <T> call(
        liveData: MutableLiveData<Resource<T>>,
        getResponse: suspend () -> Response<T>,
    ) {
        liveData.postValue(Resource.Loading)
        try {
            if (checkInternetStateUseCase.isInternetAvailable()) {
                liveData.postValue(handleCurrencyResponse(getResponse()))
            } else {
                liveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> liveData.postValue(Resource.Error("Network failure"))
                else -> liveData.postValue(Resource.Error("Conversion Error: $t"))
            }
        }
    }

    private fun <T> handleCurrencyResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            response.body()?.let { currencyResponse ->
                return Resource.Success(currencyResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun responseToList(
        currenciesLatest: CurrenciesLatest,
    ): List<CurrencyNameAndPrice> = responseMapper.map(currenciesLatest)

    //region Database
    fun saveCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job = viewModelScope.launch {
        currencyRepository.upsert(currencyNameAndPrice)
    }

    fun getSavedCurrenciesLiveData(): LiveData<List<CurrencyNameAndPrice>> =
        currencyRepository.getSavedCurrenciesLiveData()

    fun deleteSavedCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job =
        viewModelScope.launch {
            currencyRepository.deleteCurrency(currencyNameAndPrice)
        }

    fun updateCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job = viewModelScope.launch {
        currencyRepository.updateCurrency(currencyNameAndPrice)
    }
    //endregion
}



