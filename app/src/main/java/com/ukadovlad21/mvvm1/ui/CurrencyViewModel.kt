package com.ukadovlad21.mvvm1.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ukadovlad21.mvvm1.CurrencyApplication
import com.ukadovlad21.mvvm1.models.CurrenciesLatest
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CurrencyViewModel(
    app: Application,
    private val currencyRepository: CurrencyRepository
):AndroidViewModel(app) {
    val currency: MutableLiveData<Resource<CurrenciesLatest>> = MutableLiveData()


    init {
        getCurrency("usd")
    }

    fun getCurrency(valet:String) = viewModelScope.launch { safeCurrencyCall(valet) }

    private suspend fun safeCurrencyCall(valet:String){
        currency.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = currencyRepository.getCurrency(valet)
                currency.postValue(handleCurrencyResponse(response))
            }else {
                currency.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable) {
            when(t) {
                is IOException -> currency.postValue(Resource.Error("Network failure"))
                else -> currency.postValue(Resource.Error("Conversion Error ${t}"))
            }
        }
    }
    private fun handleCurrencyResponse(response: Response<CurrenciesLatest>): Resource<CurrenciesLatest> {
        if(response.isSuccessful) {
            response.body()?.let { currencyResponse ->
                return Resource.Success(currencyResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection():Boolean {
        val connectivityManager = getApplication<CurrencyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }



    fun saveCurrency(currencyNameAndPrice: CurrencyNameAndPrice) = viewModelScope.launch {
        currencyRepository.upsert(currencyNameAndPrice)
    }
    fun getSavedCurrencies() = currencyRepository.getSavedCurrencies()

    fun deleteArticle(currencyNameAndPrice: CurrencyNameAndPrice) = viewModelScope.launch {
        currencyRepository.deleteCurrency(currencyNameAndPrice)
    }

}