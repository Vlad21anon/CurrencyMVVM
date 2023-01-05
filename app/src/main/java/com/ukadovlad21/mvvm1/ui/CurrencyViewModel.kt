package com.ukadovlad21.mvvm1.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ukadovlad21.mvvm1.CurrencyApplication
import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.models.fromNameToName.CurrencyFromNameToName
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException

class CurrencyViewModel(
    app: Application,
    private val currencyRepository: CurrencyRepository,
) : AndroidViewModel(app) {
    val currency: MutableLiveData<Resource<CurrenciesLatest>> = MutableLiveData()

    val refreshCurrency: MutableLiveData<Resource<CurrencyFromNameToName>> = MutableLiveData()

    val convertCurrency: MutableLiveData<Resource<CurrencyFromNameToName>> = MutableLiveData()

    init {
        getCurrency("usd")
    }

    private fun getCurrency(valet: String) = viewModelScope.launch { safeCurrencyCall(valet) }

    fun getByNames(fromName: String, toName: String): Job = viewModelScope.launch {
        safeNameToNameCall(fromName, toName)
    }

    fun convertByNames(fromName: String, toName: String, amount: Int): Job = viewModelScope.launch {
        safeConvertCall(fromName, toName, amount)
    }

    private suspend fun safeNameToNameCall(fromName: String, toName: String) {
        refreshCurrency.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = currencyRepository.getCurrencyNameToName(fromName, toName, 1)
                refreshCurrency.postValue(handleCurrencyResponse(response))
            } else {
                refreshCurrency.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> refreshCurrency.postValue(Resource.Error("Network failure"))
                else -> refreshCurrency.postValue(Resource.Error("Conversion Error: $t"))
            }
        }
    }

    private suspend fun safeConvertCall(fromName: String, toName: String, amount: Int) {
        convertCurrency.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = currencyRepository.getCurrencyNameToName(fromName, toName, amount)
                convertCurrency.postValue(handleCurrencyResponse(response))
            } else {
                convertCurrency.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> convertCurrency.postValue(Resource.Error("Network failure"))
                else -> convertCurrency.postValue(Resource.Error("Conversion Error: $t"))
            }
        }
    }

    private suspend fun safeCurrencyCall(valet: String) {
        currency.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = currencyRepository.getCurrency(valet)
                currency.postValue(handleCurrencyResponse(response))
            } else {
                currency.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> currency.postValue(Resource.Error("Network failure"))
                else -> currency.postValue(Resource.Error("Conversion Error: $t"))
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

    //region Internet Check
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CurrencyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
    //endregion

    fun responseToList(
        rates: Map<String, Double>,
        data: String,
    ): List<CurrencyNameAndPrice> {
        val list: MutableList<CurrencyNameAndPrice> = mutableListOf()
        val savedCurrenciesList = getSavedCurrencies()
        rates.forEach { (key, value) ->
            val item = CurrencyNameAndPrice(
                null,
                key,
                value,
                data,
                isSavedToDb(savedCurrenciesList, key)
            )
            list.add(item)
        }
        return list
    }

    private fun isSavedToDb(list: List<CurrencyNameAndPrice>, name: String): Boolean {
        list.forEach {
            if (it.name == name) return true
        }
        return false
    }


    //region Database
    fun saveCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job = viewModelScope.launch {
        currencyRepository.upsert(currencyNameAndPrice)
    }

    fun getSavedCurrenciesLiveData(): LiveData<List<CurrencyNameAndPrice>> =
        currencyRepository.getSavedCurrenciesLiveData()

    fun getSavedCurrencies(): List<CurrencyNameAndPrice> = runBlocking {
        currencyRepository.getSavedCurrencies()
    }

    fun deleteSavedCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job =
        viewModelScope.launch {
            currencyRepository.deleteCurrency(currencyNameAndPrice)
        }

    fun updateCurrency(currencyNameAndPrice: CurrencyNameAndPrice): Job = viewModelScope.launch {
        currencyRepository.updateCurrency(currencyNameAndPrice)
    }
    //endregion
}



