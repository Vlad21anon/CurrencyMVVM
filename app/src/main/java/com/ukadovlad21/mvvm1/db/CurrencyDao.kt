package com.ukadovlad21.mvvm1.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(currencyNameAndPrice: CurrencyNameAndPrice): Long

    @Query("SELECT * FROM currencies ")
    fun getAllSavedCurrenciesLiveData(): LiveData<List<CurrencyNameAndPrice>>


    @Query("SELECT * FROM currencies ")
    suspend fun getAllSavedCurrencies(): List<CurrencyNameAndPrice>


    @Delete
    suspend fun deleteCurrency(currencyNameAndPrice: CurrencyNameAndPrice)

    @Query("UPDATE currencies SET price=:priceIt , actualAt=:actualAt WHERE name = :nameIt")
    suspend fun updateCurrency(nameIt: String, priceIt: Double, actualAt: String)
}
